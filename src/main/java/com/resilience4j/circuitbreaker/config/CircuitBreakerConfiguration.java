package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
@Slf4j
public class CircuitBreakerConfiguration implements Supplier<CircuitBreakerConfig> {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String COUNTRY_CB_NAME = "countries-service";
    private final CircuitBreakerProperties properties;

    public CircuitBreakerConfiguration(CircuitBreakerProperties properties) {
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
