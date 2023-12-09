package com.resilience4j.circuitbreaker.endpoint;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.Builder;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Endpoint(id = "cbcountry")
@Component
public class CountriesEndpoint {
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CountriesEndpoint(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @ReadOperation
    public CircuitBreakerParam getCircuitBreakerProperties() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("countries-service");
        CircuitBreakerConfig config = circuitBreaker.getCircuitBreakerConfig();
        return CircuitBreakerParam.builder()
                .slidingWindowSize(config.getSlidingWindowSize())
                .minimumNumberOfCalls(config.getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(config.getPermittedNumberOfCallsInHalfOpenState())
                .automaticTransitionFromOpenToHalfOpenEnabled(config.isAutomaticTransitionFromOpenToHalfOpenEnabled())
                .slowCallRateThreshold(config.getSlowCallRateThreshold())
                .failureRateThreshold(config.getFailureRateThreshold())
                .slowCallDurationThreshold(config.getSlowCallDurationThreshold())
                .build();
    }

    @Builder
    public record CircuitBreakerParam(Integer slidingWindowSize,
                               Integer minimumNumberOfCalls,
                               Integer permittedNumberOfCallsInHalfOpenState,
                               Boolean automaticTransitionFromOpenToHalfOpenEnabled,
                               Float slowCallRateThreshold,
                               Float failureRateThreshold,
                               Duration slowCallDurationThreshold) {

    }
}
