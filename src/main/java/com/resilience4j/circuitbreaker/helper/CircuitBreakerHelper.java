package com.resilience4j.circuitbreaker.helper;

import com.resilience4j.circuitbreaker.annotation.CustomCircuitBreaker;
import com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration;
import com.resilience4j.circuitbreaker.service.CallFallback;
import com.resilience4j.circuitbreaker.service.CountriesService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@Aspect
@EnableAspectJAutoProxy
public class CircuitBreakerHelper {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String COUNTRY_CB_NAME = "countries-service";
    private final CircuitBreakerConfiguration configuration;
    private final CountriesService countriesService;
    private CustomCircuitBreaker customCircuitBreaker;
    private CallFallback callFallback;

    public CircuitBreakerHelper(CircuitBreakerConfiguration configuration, CountriesService countriesService) {
        this.configuration = configuration;
        this.countriesService = countriesService;
    }
    @Around("@annotation(com.resilience4j.circuitbreaker.annotation.CustomCircuitBreaker)")
    public List<Object> createCircuitBreaker(ProceedingJoinPoint pjp) throws Throwable {
        CircuitBreakerRegistry registry = configuration.getRegistry();

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = pjp.getTarget()
                .getClass()
                .getMethod(signature.getMethod().getName(),
                        signature.getMethod().getParameterTypes());
        customCircuitBreaker = method.getAnnotation(CustomCircuitBreaker.class);

        callFallback = customCircuitBreaker.fallbackMethod().newInstance();

        return Decorators.ofSupplier(countriesService::getCountries)
                .withCircuitBreaker(registry.circuitBreaker(COUNTRY_CB_NAME, CB_COUNTRY_CONFIG))
                .withFallback(this::getAlternateMessage)
                .decorate()
                    .get();

    }

    public List<Object> getAlternateMessage(Throwable throwable) {
        return callFallback.getFallback(throwable);
    }
}
