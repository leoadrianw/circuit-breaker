package com.resilience4j.circuitbreaker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.albumcb")
public record CircuitBreakerProperties(
        Integer slidingWindowSize,
        Integer minimumNumberOfCalls,
        Integer permittedNumberOfCallsInHalfOpenState,
        Boolean automaticTransitionFromOpenToHalfOpenEnabled,
        Float slowCallRateThreshold,
        Float failureRateThreshold,
        Duration slowCallDurationThreshold) {
}
