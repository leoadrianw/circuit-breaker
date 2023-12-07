package com.resilience4j.circuitbreaker.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.countries-service")
@Data
public class CircuitBreakerProperties {
    private Integer slidingWindowSize;
    private Integer minimumNumberOfCalls;
    private Integer permittedNumberOfCallsInHalfOpenState;
    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;
    private Float slowCallRateThreshold;
    private Float failureRateThreshold;
    private Duration slowCallDurationThreshold;
}
