package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.functions.CheckedFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DelegateCircuitBreaker {
    String name() default "";

    String serviceCode() default "0";

    String configName() default "customDefault";

    Class<? extends CheckedFunction<Throwable, ?>> fallback();

    Class<? extends Supplier<CircuitBreakerConfig>> configuration();

    Class<? extends Throwable>[] ignoreExceptions() default {};
}