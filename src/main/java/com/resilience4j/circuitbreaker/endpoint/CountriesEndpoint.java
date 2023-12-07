package com.resilience4j.circuitbreaker.endpoint;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.Builder;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Endpoint(id="cbcountry")
@Component
public class CountriesEndpoint {
    private final CircuitBreaker circuitBreaker;

    public CountriesEndpoint(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @ReadOperation
    public CircuitBreakerParam getCircuitBreakerProperties() {
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

}
