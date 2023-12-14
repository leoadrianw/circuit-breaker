package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
@Slf4j
public class IgnoreCBConfiguration implements Supplier<CircuitBreakerConfig> {
    public static final String IGNORE_CONFIG = "ignoreCB";
    public static final String IGNORE_CB_NAME = "ignore-cb";
    private final IgnoreCBProperties properties;

    public IgnoreCBConfiguration(IgnoreCBProperties properties) {
        this.properties = properties;
    }

    public CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(properties.getSlidingWindowSize())
                .minimumNumberOfCalls(properties.getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(properties.getPermittedNumberOfCallsInHalfOpenState())
                .failureRateThreshold(properties.getFailureRateThreshold())
                .slowCallRateThreshold(properties.getSlowCallRateThreshold())
                .slowCallDurationThreshold(properties.getSlowCallDurationThreshold())
                .waitDurationInOpenState(Duration.ofSeconds(10L))
                .build();
    }

    @Override
    public CircuitBreakerConfig get() {
        return this.getConfig();
    }
}
