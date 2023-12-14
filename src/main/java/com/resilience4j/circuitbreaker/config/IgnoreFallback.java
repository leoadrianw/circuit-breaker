package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.core.functions.CheckedFunction;
import org.springframework.stereotype.Component;

@Component
public class IgnoreFallback implements CheckedFunction<Throwable, String> {

    @Override
    public String apply(Throwable throwable) throws Throwable {
        return "This endpoint have exception " + throwable.getCause() + " than ignore it.";
    }
}
