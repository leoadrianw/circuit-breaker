package com.resilience4j.circuitbreaker.endpoint;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
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
        CircuitBreakerParam circuitBreakerParam = CircuitBreakerParam.builder()
                .slidingWindowSize(circuitBreaker.getCircuitBreakerConfig().getSlidingWindowSize())
                .minimumNumberOfCalls(circuitBreaker.getCircuitBreakerConfig().getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(circuitBreaker.getCircuitBreakerConfig().getPermittedNumberOfCallsInHalfOpenState())
                .automaticTransitionFromOpenToHalfOpenEnabled(circuitBreaker.getCircuitBreakerConfig().isAutomaticTransitionFromOpenToHalfOpenEnabled())
                .slowCallRateThreshold(circuitBreaker.getCircuitBreakerConfig().getSlowCallRateThreshold())
                .failureRateThreshold(circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold())
                .slowCallDurationThreshold(circuitBreaker.getCircuitBreakerConfig().getSlowCallDurationThreshold())
                .build();
        return circuitBreakerParam;
    }

    @Builder
    record CircuitBreakerParam(Integer slidingWindowSize,
                               Integer minimumNumberOfCalls,
                               Integer permittedNumberOfCallsInHalfOpenState,
                               Boolean automaticTransitionFromOpenToHalfOpenEnabled,
                               Float slowCallRateThreshold,
                               Float failureRateThreshold,
                               Duration slowCallDurationThreshold) {

    }

    @Builder
    record CircuitBreakerMetrics(Integer totalCallNumber,
                                 String state,
                                 Float failureRate,
                                 Integer notPermittedCalls,
                                 Integer slowCalls,
                                 Integer successfulCalls) {

    }


}
