package com.resilience4j.circuitbreaker.annotation;

import com.resilience4j.circuitbreaker.service.CallFallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CustomCircuitBreaker {
    Class<? extends Throwable>[] ignoreExceptions() default {};
    Class<? extends CallFallback> fallbackMethod();
}
