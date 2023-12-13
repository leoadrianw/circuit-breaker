package com.resilience4j.circuitbreaker.config;

import com.resilience4j.circuitbreaker.constraint.annotation.DelegateCircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
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
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Aspect
@Component
@Slf4j
public class DelegateCircuitBreakerAspect implements Ordered {
    private final CircuitBreakerRegistry registry;
    private final CircuitBreakerConfigurationProperties circuitBreakerProperties;

    public DelegateCircuitBreakerAspect(CircuitBreakerConfigurationProperties circuitBreakerProperties) {
        this.registry = CircuitBreakerRegistry.ofDefaults();
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
        var method = ((MethodSignature) pjp.getSignature()).getMethod();

        if (Objects.isNull(delegateCircuitBreakerAnnotation)) {
            delegateCircuitBreakerAnnotation = this.getCircuitBreakerAnnotation(pjp);
        }
        if (Objects.isNull(delegateCircuitBreakerAnnotation)) {
            return pjp.proceed();
        }



        CircuitBreaker circuitBreaker = this.getCircuitBreaker(delegateCircuitBreakerAnnotation);

        CheckedSupplier<Object> circuitBreakerExecution = () -> this.defaultHandling(pjp, circuitBreaker);
        var decorate = Decorators.ofCheckedSupplier(circuitBreakerExecution)
                .decorate();

        return decorate.get();
    }

    private CircuitBreaker getCircuitBreaker(DelegateCircuitBreaker annotation) throws Exception {
        String name = annotation.name();
        String configName = annotation.configName();
        String serviceCode = annotation.serviceCode();visual-studio-code-bin
        var configuration = annotation.configuration();
        Callable<?> fallback = annotation.fallback().getDeclaredConstructor().newInstance();

        Supplier<CircuitBreakerConfig> configSupplier = createSupplierInstance(configuration);

        if (registry.getConfiguration(configName).isEmpty()) {
            registry.addConfiguration(configName, configSupplier.get());
        }

        return registry.circuitBreaker(name, configName);
    }

//    private Object proceed(ProceedingJoinPoint pjp,
//                           CircuitBreaker circuitBreaker,
//                           Class<?> returnType) throws Throwable {
//        if (CompletionStage.class.isAssignableFrom(returnType)) {
//            return handleJoinPointCompletableFuture(pjp, circuitBreaker);
//        }
//
//        return defaultHandling(pjp, circuitBreaker);
//    }

    private Object defaultHandling(ProceedingJoinPoint pjp,
                                   CircuitBreaker circuitBreaker) throws Throwable {
        return circuitBreaker.executeCheckedSupplier(pjp::proceed);
    }

//    private Object handleJoinPointCompletableFuture(ProceedingJoinPoint pjp,
//                                                    CircuitBreaker circuitBreaker) {
//        return circuitBreaker.executeCompletionStage(() -> {
//            try {
//                return (CompletionStage<?>) pjp.proceed();
//            } catch (Throwable e) {
//                throw new CompletionException(e);
//            }
//        });
//    }

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

    @Override
    public int getOrder() {
        return circuitBreakerProperties.getCircuitBreakerAspectOrder();
    }

    //    @Nullable
    private Supplier<CircuitBreakerConfig> createSupplierInstance(Class<? extends Supplier<CircuitBreakerConfig>> configuration) {
        Class<?> type = BeanUtils.findPropertyType(configuration.getName(), configuration);
        type.getConstructor()
//        if (type instanceof ParameterizedType parameterizedType) {
//            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        // Assuming there's only one type argument (CircuitBreakerConfig), you can get it like this:
//            Type typeArgument = typeArguments[0];
//            if (typeArgument instanceof Class) {
//                var supplierClass = (Class<? extends Supplier<CircuitBreakerConfig>>) typeArgument;

        // Now you can use supplierClass as a Supplier class with its type parameter
//                try {
        return null;
//                } catch (Exception e) {
//                    throw new IllegalStateException(e);
//                }
        // Use supplierInstance as needed
    }
//        }

//        return null;
    //}
}

