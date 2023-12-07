package com.resilience4j.circuitbreaker.service;

import com.resilience4j.circuitbreaker.properties.CircuitBreakerProperties;
import com.resilience4j.circuitbreaker.request.CircuitBreakerRequest;
import com.resilience4j.circuitbreaker.response.CircuitBreakerResponse;
import org.springframework.stereotype.Service;

@Service
public class ChangeConfigService {
    private final CircuitBreakerProperties circuitBreakerProperties;

    public ChangeConfigService(CircuitBreakerProperties circuitBreakerProperties) {
        this.circuitBreakerProperties = circuitBreakerProperties;
    }

    public CircuitBreakerResponse setCircuitBreakerProperties(CircuitBreakerRequest request) {
        if (request.getSlidingWindowSize() != null) {
            circuitBreakerProperties.setSlidingWindowSize(request.getSlidingWindowSize());
        }
        if (request.getMinimumNumberOfCalls() != null) {
            circuitBreakerProperties.setMinimumNumberOfCalls(request.getMinimumNumberOfCalls());
        }
        if (request.getPermittedNumberOfCallsInHalfOpenState() != null) {
            circuitBreakerProperties.setPermittedNumberOfCallsInHalfOpenState(request.getPermittedNumberOfCallsInHalfOpenState());
        }
        if (request.getAutomaticTransitionFromOpenToHalfOpenEnabled() != null) {
            circuitBreakerProperties.setAutomaticTransitionFromOpenToHalfOpenEnabled(request.getAutomaticTransitionFromOpenToHalfOpenEnabled());
        }
        if (request.getSlowCallRateThreshold() != null) {
            circuitBreakerProperties.setSlowCallRateThreshold(request.getSlowCallRateThreshold());
        }
        if (request.getFailureRateThreshold() != null) {
            circuitBreakerProperties.setFailureRateThreshold(request.getFailureRateThreshold());
        }
        if (request.getSlowCallDurationThreshold() != null) {
            circuitBreakerProperties.setSlowCallDurationThreshold(request.getSlowCallDurationThreshold());
        }

        return getCircuitBreakerResponse(circuitBreakerProperties);
    }

    private CircuitBreakerResponse getCircuitBreakerResponse(CircuitBreakerProperties circuitBreakerProperties) {
        CircuitBreakerResponse response = new CircuitBreakerResponse();
        response.setSlidingWindowSize(circuitBreakerProperties.getSlidingWindowSize());
        response.setMinimumNumberOfCalls(circuitBreakerProperties.getMinimumNumberOfCalls());
        response.setPermittedNumberOfCallsInHalfOpenState(circuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState());
        response.setAutomaticTransitionFromOpenToHalfOpenEnabled(circuitBreakerProperties.getAutomaticTransitionFromOpenToHalfOpenEnabled());
        response.setSlowCallRateThreshold(circuitBreakerProperties.getSlowCallRateThreshold());
        response.setFailureRateThreshold(circuitBreakerProperties.getFailureRateThreshold());
        response.setSlowCallDurationThreshold(circuitBreakerProperties.getSlowCallDurationThreshold());
        return response;
    }

}
