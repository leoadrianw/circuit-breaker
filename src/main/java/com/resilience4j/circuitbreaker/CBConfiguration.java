package com.resilience4j.circuitbreaker;

import com.resilience4j.circuitbreaker.config.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
public class CBConfiguration implements Supplier<CircuitBreakerConfig> {
    private final CircuitBreakerProperties properties;

    public CBConfiguration(CircuitBreakerProperties properties) {this.properties = properties;}


    @Override
    public CircuitBreakerConfig get() {
        return this.getConfig();
    }

    public CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(properties.slidingWindowSize())
                .minimumNumberOfCalls(properties.minimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(properties.permittedNumberOfCallsInHalfOpenState())
                .failureRateThreshold(properties.failureRateThreshold())
                .slowCallRateThreshold(properties.slowCallRateThreshold())
                .slowCallDurationThreshold(properties.slowCallDurationThreshold())
                .waitDurationInOpenState(Duration.ofSeconds(10L))
                .build();
    }
}
