package com.resilience4j.circuitbreaker.config;

import com.resilience4j.circuitbreaker.domain.CbConfig;
import com.resilience4j.circuitbreaker.repository.CircuitBreakerRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String COUNTRY_CB_NAME = "countries-service";
    private final CircuitBreakerRepository repository;

    public CircuitBreakerConfiguration(CircuitBreakerRepository repository) {
        this.repository = repository;
    }

    @Order(1)
    @PostConstruct
    public void run() {
        if (repository.findById(1L).isPresent()) {
            return;
        }

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
        repository.saveAndFlush(entity);
    }

    public CircuitBreakerConfig getConfig() {
        CbConfig config = repository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);

        return CircuitBreakerConfig
                .custom()
                .slidingWindowType(config.getSlidingWindowType())
                .automaticTransitionFromOpenToHalfOpenEnabled(config.getAutomaticTransitionFromOpenToHalfOpenEnabled())
                .slidingWindowSize(config.getSlidingWindowSize())
                .minimumNumberOfCalls(config.getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(config.getPermittedNumberOfCallsInHalfOpenState())
                .failureRateThreshold(config.getFailureRateThreshold())
                .slowCallRateThreshold(config.getSlowCallRateThreshold())
                .slowCallDurationThreshold(config.getSlowCallDurationThreshold())
                .waitDurationInOpenState(config.getWaitDurationInOpenState())
                .build();
    }

    @Bean
    @Order(99)
    public CircuitBreakerRegistry getRegistry() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        circuitBreakerRegistry.addConfiguration(CB_COUNTRY_CONFIG, this.getConfig());
        return circuitBreakerRegistry;
    }
}
