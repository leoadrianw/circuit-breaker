package com.resilience4j.circuitbreaker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.ignore-cb")
public class IgnoreCBProperties {
    private Integer slidingWindowSize;
    private Integer minimumNumberOfCalls;
    private Integer permittedNumberOfCallsInHalfOpenState;
    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;
    private Float slowCallRateThreshold;
    private Float failureRateThreshold;
    private Duration slowCallDurationThreshold;
}

