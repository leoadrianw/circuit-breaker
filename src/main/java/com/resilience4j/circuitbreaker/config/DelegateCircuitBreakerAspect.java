package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.core.lang.Nullable;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.spring6.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import io.github.resilience4j.spring6.utils.AnnotationExtractor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class DelegateCircuitBreakerAspect implements Ordered {
    private final CircuitBreakerConfigurationProperties circuitBreakerProperties;
    private final ApplicationContext context;

    public DelegateCircuitBreakerAspect(CircuitBreakerConfigurationProperties circuitBreakerProperties,
                                        ApplicationContext context) {
        this.context = context;
        this.circuitBreakerProperties = circuitBreakerProperties;
    }

    @Pointcut(
            value = "@within(delegateCircuitBreaker) || @annotation(delegateCircuitBreaker)",
            argNames = "delegateCircuitBreaker"
    )
    public void delegateCircuitBreakerPointCut(DelegateCircuitBreaker delegateCircuitBreaker) {}

    @Around(
            value = "delegateCircuitBreakerPointCut(delegateCircuitBreakerAnnotation)",
            argNames = "pjp, delegateCircuitBreakerAnnotation"
    )
    public Object delegateCBAroundAdvice(ProceedingJoinPoint pjp,
                                         @Nullable DelegateCircuitBreaker delegateCircuitBreakerAnnotation) throws Throwable {
        if (Objects.isNull(delegateCircuitBreakerAnnotation)) {
            delegateCircuitBreakerAnnotation = this.getCircuitBreakerAnnotation(pjp);
        }
        if (Objects.isNull(delegateCircuitBreakerAnnotation)) {
            return pjp.proceed();
        }

        var circuitBreaker = this.getCircuitBreaker(delegateCircuitBreakerAnnotation);
        var serviceCode = delegateCircuitBreakerAnnotation.serviceCode();

        this.circuitEventPublisher(circuitBreaker, serviceCode);

        var fallback = context.getBean(delegateCircuitBreakerAnnotation.fallback());
        CheckedSupplier<Object> circuitBreakerExecution = () -> this.defaultHandling(pjp, circuitBreaker);

        return Decorators.ofCheckedSupplier(circuitBreakerExecution)
                .withFallback(fallback::apply)
                .decorate()
                .get();
    }

    private void circuitEventPublisher(CircuitBreaker circuitBreaker, String serviceCode) {
        circuitBreaker.getEventPublisher()
                .onCallNotPermitted(e -> log.warn("Service in {}. {}", serviceCode, e.getEventType() ))
                .onFailureRateExceeded(e -> log.warn("Service in {}, have failure rate: {}", serviceCode, e.getFailureRate()));
    }

    @Override
    public int getOrder() {
        return circuitBreakerProperties.getCircuitBreakerAspectOrder();
    }


    @Bean
    @RefreshScope
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    private CircuitBreaker getCircuitBreaker(DelegateCircuitBreaker annotation) throws Exception {
        CircuitBreakerRegistry registry = context.getBean(CircuitBreakerRegistry.class);
        var name = annotation.name();
        var configName = annotation.configName();
        var configuration = annotation.configuration();
        var configSupplier = context.getBean(configuration);

        if (registry.getConfiguration(configName).isEmpty()) {
            registry.addConfiguration(configName, configSupplier.get());
        }

        return registry.circuitBreaker(name, configName);
    }

    private Object defaultHandling(ProceedingJoinPoint pjp,
                                   CircuitBreaker circuitBreaker) throws Throwable {
        return circuitBreaker.executeCheckedSupplier(pjp::proceed);
    }

    @Nullable
    private DelegateCircuitBreaker getCircuitBreakerAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("circuitBreaker parameter is null");
        }
        if (proceedingJoinPoint.getTarget() instanceof Proxy) {
            log.debug("The circuit breaker annotation is kept on a interface which is acting as a proxy");
            return AnnotationExtractor
                    .extractAnnotationFromProxy(
                            proceedingJoinPoint.getTarget(),
                            DelegateCircuitBreaker.class
                    );
        }

        return AnnotationExtractor.extract(
                proceedingJoinPoint.getTarget().getClass(),
                DelegateCircuitBreaker.class
        );
    }
}

