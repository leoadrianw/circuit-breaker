package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.github.resilience4j.springboot3.circuitbreaker.monitoring.endpoint.CircuitBreakerEventsEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    private final CircuitBreakerProperties properties;

    public CircuitBreakerConfiguration(CircuitBreakerProperties properties) {
        this.properties = properties;
    }

    public CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(properties.getSlidingWindowSize())
                .minimumNumberOfCalls(properties.getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(properties.getPermittedNumberOfCallsInHalfOpenState())
                .failureRateThreshold(properties.getFailureRateThreshold())
                .slowCallRateThreshold(properties.getSlowCallRateThreshold())
                .slowCallDurationThreshold(properties.getSlowCallDurationThreshold())
                .waitDurationInOpenState(Duration.ofSeconds(10L))
                .build();
    }

    @Bean
    @RefreshScope
    public CircuitBreakerRegistry getRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        registry.addConfiguration(CB_COUNTRY_CONFIG, this.getConfig());
        return registry;
    }

}
