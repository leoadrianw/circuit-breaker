package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String COUNTRY_CB_NAME = "countries-service";
    private final CircuitBreakerProperties properties;

    public CircuitBreakerConfiguration(CircuitBreakerProperties properties) {this.properties = properties;}

    public CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(properties.slidingWindowSize())
                .minimumNumberOfCalls(properties.minimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(properties.permittedNumberOfCallsInHalfOpenState())
                .failureRateThreshold(properties.failureRateThreshold())
                .slowCallRateThreshold(properties.slowCallRateThreshold())
                .slowCallDurationThreshold(properties.slowCallDurationThreshold())
                .waitDurationInOpenState(Duration.ofSeconds(10L))
                .build();
    }

    @Bean
    public CircuitBreakerRegistry getRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        registry.addConfiguration(CB_COUNTRY_CONFIG, this.getConfig());
        return registry;
    }
}
