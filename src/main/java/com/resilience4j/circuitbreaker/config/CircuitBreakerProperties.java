package com.resilience4j.circuitbreaker.config;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Builder
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.countries-service")
public record CircuitBreakerProperties(
        Integer slidingWindowSize,
        Integer minimumNumberOfCalls,
        Integer permittedNumberOfCallsInHalfOpenState,
        Boolean automaticTransitionFromOpenToHalfOpenEnabled,
        Float slowCallRateThreshold,
        Float failureRateThreshold,
        Duration slowCallDurationThreshold) {
}
