package com.resilience4j.circuitbreaker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.countries-service")
public class CircuitBreakerProperties {
    private Integer slidingWindowSize;
    private Integer minimumNumberOfCalls;
    private Integer permittedNumberOfCallsInHalfOpenState;
    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;
    private Float slowCallRateThreshold;
    private Float failureRateThreshold;
    private Duration slowCallDurationThreshold;
}

