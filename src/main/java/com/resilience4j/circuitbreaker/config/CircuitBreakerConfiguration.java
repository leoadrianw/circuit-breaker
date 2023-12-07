package com.resilience4j.circuitbreaker.config;

import com.resilience4j.circuitbreaker.domain.CbConfig;
import com.resilience4j.circuitbreaker.repository.CircuitBreakerRepository;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    private final CircuitBreakerRepository circuitBreakerRepository;

    public CircuitBreakerConfiguration(CircuitBreakerRepository circuitBreakerRepository) {
        this.circuitBreakerRepository = circuitBreakerRepository;
    }

    @Bean
    @Order(99)
    public CommandLineRunner run(CircuitBreakerRepository repository) {
        return args -> {
            if (repository.findById(1L).isPresent()) {
                return;
            } else {
                var entity = new CbConfig();
                entity.setId(1L);
                entity.setSlidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED);
                entity.setSlidingWindowSize(10);
                entity.setMinimumNumberOfCalls(5);
                entity.setFailureRateThreshold(30.0F);
                entity.setPermittedNumberOfCallsInHalfOpenState(3);
                entity.setAutomaticTransitionFromOpenToHalfOpenEnabled(true);
                entity.setSlowCallRateThreshold(80.0f);
                entity.setSlowCallDurationThreshold(Duration.ofSeconds(3));
                entity.setWaitDurationInOpenState(Duration.ofSeconds(5));
                repository.save(entity);
            }

        };
    }

    private CircuitBreakerConfig getConfig() {
        CbConfig config = circuitBreakerRepository.findById(Long.valueOf(1)).get();
        return CircuitBreakerConfig
                .custom()
                .slidingWindowType(config.getSlidingWindowType())
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(config.getSlidingWindowSize())
                .minimumNumberOfCalls(config.getMinimumNumberOfCalls())
                .failureRateThreshold(config.getFailureRateThreshold())
                .slowCallRateThreshold(config.getSlowCallRateThreshold())
                .slowCallDurationThreshold(config.getSlowCallDurationThreshold())
                .waitDurationInOpenState(config.getWaitDurationInOpenState())
                .build();
    }

    @Bean
    @Order(1)
    public CircuitBreakerRegistry getRegistry() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(getConfig());
        circuitBreakerRegistry.circuitBreaker("countries-service");
        return circuitBreakerRegistry;
    }



//    @Bean
//    public MeterRegistry getMetrics() {
//        MeterRegistry meterRegistry = new SimpleMeterRegistry();
//        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(getRegistry())
//                .bindTo(meterRegistry);
//
//        return meterRegistry;
//    }
//
//
//    @Bean
//    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
//        return new RegistryEventConsumer<CircuitBreaker>() {
//
//            @Override
//            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
//                entryAddedEvent.getAddedEntry().getEventPublisher()
//                        .onFailureRateExceeded(event -> log.error("circuit breaker {} failure rate {} on {}",
//                                event.getCircuitBreakerName(), event.getFailureRate(), event.getCreationTime())
//                        )
//                        .onSlowCallRateExceeded(event -> log.error("circuit breaker {} slow call rate {} on {}",
//                                event.getCircuitBreakerName(), event.getSlowCallRate(), event.getCreationTime())
//                        )
//                        .onCallNotPermitted(event -> log.error("circuit breaker {} call not permitted {}",
//                                event.getCircuitBreakerName(), event.getCreationTime())
//                        )
//                        .onError(event -> log.error("circuit breaker {} error with duration {}s",
//                                event.getCircuitBreakerName(), event.getElapsedDuration().getSeconds())
//                        )
//                        .onStateTransition(
//                                event -> log.warn("circuit breaker {} state transition from {} to {} on {}",
//                                        event.getCircuitBreakerName(), event.getStateTransition().getFromState(),
//                                        event.getStateTransition().getToState(), event.getCreationTime())
//                        );
//            }
//
//            @Override
//            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
//                entryRemoveEvent.getRemovedEntry().getEventPublisher()
//                        .onFailureRateExceeded(event -> log.debug("Circuit breaker event removed {}",
//                                event.getCircuitBreakerName()));
//            }
//
//            @Override
//            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
//                entryReplacedEvent.getNewEntry().getEventPublisher()
//                        .onFailureRateExceeded(event -> log.debug("Circuit breaker event replaced {}",
//                                event.getCircuitBreakerName()));
//            }
//        };
//    }
}
