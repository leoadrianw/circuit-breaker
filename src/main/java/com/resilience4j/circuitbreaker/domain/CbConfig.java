package com.resilience4j.circuitbreaker.domain;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Entity
@Data
@Table(name = "cb_config")
public class CbConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sliding_window_type")
    @Enumerated(EnumType.STRING)
    private SlidingWindowType slidingWindowType;

    @Column(name = "sliding_window_size")
    private Integer slidingWindowSize;

    @Column(name = "minimum_number_of_calls")
    private Integer minimumNumberOfCalls;

    @Column(name = "permitted_number_of_calls_in_half_open_state")
    private Integer permittedNumberOfCallsInHalfOpenState;

    @Column(name = "automatic_transition_from_open_to_half_open_enabled")
    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;

    @Column(name = "slow_call_rate_threshold")
    private Float slowCallRateThreshold;

    @Column(name = "failure_rate_threshold")
    private Float failureRateThreshold;

    @Column(name = "slow_call_duration_threshold")
    private Duration slowCallDurationThreshold;

    @Column(name = "wait_duration_in_open_state")
    private Duration waitDurationInOpenState;
}
