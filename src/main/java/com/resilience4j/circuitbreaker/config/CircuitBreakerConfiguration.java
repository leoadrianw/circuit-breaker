package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    private CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50.0f)
                .slowCallRateThreshold(70.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public CircuitBreakerRegistry getRegistry() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(getConfig());
        circuitBreakerRegistry.circuitBreaker("countries-service");
        return circuitBreakerRegistry;
    }

    @Bean
    public MeterRegistry getMetrics() {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(getRegistry())
                .bindTo(meterRegistry);

        return meterRegistry;
    }

    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<CircuitBreaker>() {

            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onFailureRateExceeded(event -> log.error("circuit breaker {} failure rate {} on {}",
                                event.getCircuitBreakerName(), event.getFailureRate(), event.getCreationTime())
                        )
                        .onSlowCallRateExceeded(event -> log.error("circuit breaker {} slow call rate {} on {}",
                                event.getCircuitBreakerName(), event.getSlowCallRate(), event.getCreationTime())
                        )
                        .onCallNotPermitted(event -> log.error("circuit breaker {} call not permitted {}",
                                event.getCircuitBreakerName(), event.getCreationTime())
                        )
                        .onError(event -> log.error("circuit breaker {} error with duration {}s",
                                event.getCircuitBreakerName(), event.getElapsedDuration().getSeconds())
                        )
                        .onStateTransition(
                                event -> log.warn("circuit breaker {} state transition from {} to {} on {}",
                                        event.getCircuitBreakerName(), event.getStateTransition().getFromState(),
                                        event.getStateTransition().getToState(), event.getCreationTime())
                        );
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher()
                        .onFailureRateExceeded(event -> log.debug("Circuit breaker event removed {}",
                                event.getCircuitBreakerName()));
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher()
                        .onFailureRateExceeded(event -> log.debug("Circuit breaker event replaced {}",
                                event.getCircuitBreakerName()));
            }
        };
    }
}
