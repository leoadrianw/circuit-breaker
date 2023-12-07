package com.resilience4j.circuitbreaker.endpoint;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Builder;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

@Endpoint(id = "cbcountry")
@Component
public class CountriesEndpoint {
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final MeterRegistry meterRegistry;

    public CountriesEndpoint(CircuitBreakerRegistry circuitBreakerRegistry, MeterRegistry meterRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.meterRegistry = meterRegistry;
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
