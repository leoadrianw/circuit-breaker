package com.resilience4j.circuitbreaker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Duration;

@Data
public class CircuitBreakerRequest {
    @JsonProperty(value = "sliding_window_size")
    private Integer slidingWindowSize;
    @JsonProperty(value = "minimum_number_of_calls")
    private Integer minimumNumberOfCalls;
    @JsonProperty(value = "permitted_number_of_calls_in_half_open_state")
    private Integer permittedNumberOfCallsInHalfOpenState;
    @JsonProperty(value = "automatic_transition_from_open_to_half_open_enabled")
    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;
    @JsonProperty(value = "slow_call_rate_threshold")
    private Float slowCallRateThreshold;
    @JsonProperty(value = "failure_rate_threshold")
    private Float failureRateThreshold;
    @JsonProperty(value = "slow_call_duration_threshold")
    private Duration slowCallDurationThreshold;
}
