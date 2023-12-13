package com.resilience4j.circuitbreaker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;

@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.countries-service")
@RefreshScope
public record CircuitBreakerProperties(
        Integer slidingWindowSize,
        Integer minimumNumberOfCalls,
        Integer permittedNumberOfCallsInHalfOpenState,
        Boolean automaticTransitionFromOpenToHalfOpenEnabled,
        Float slowCallRateThreshold,
        Float failureRateThreshold,
        Duration slowCallDurationThreshold) {
}
