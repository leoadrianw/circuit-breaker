package com.resilience4j.circuitbreaker.endpoint;

import com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.Builder;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Endpoint(id = "cbcountry")
@Component
public class CountriesEndpoint {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String CB_COUNTRY = "countries-service";
    private final CircuitBreakerConfiguration configuration;

    public CountriesEndpoint(CircuitBreakerConfiguration configuration) {
        this.configuration = configuration;
    }

    public CircuitBreakerConfig updateConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(50)
                .minimumNumberOfCalls(80)
                .permittedNumberOfCallsInHalfOpenState(3)
                .failureRateThreshold(40)
                .slowCallRateThreshold(40)
                .slowCallDurationThreshold(Duration.ofSeconds(15))
                .waitDurationInOpenState(Duration.ofSeconds(10L))
                .build();
    }

    @ReadOperation
    @RefreshScope
    public CircuitBreakerParam getCircuitBreakerProperties() {
        CircuitBreakerRegistry registry = configuration.getRegistry();
        CircuitBreakerConfig config = updateConfig();
        registry.addConfiguration(CB_COUNTRY_CONFIG, config);
        return CircuitBreakerParam.builder()
                .slidingWindowSize(config.getSlidingWindowSize())
                .minimumNumberOfCalls(config.getMinimumNumberOfCalls())
                .permittedNumberOfCallsInHalfOpenState(config.getPermittedNumberOfCallsInHalfOpenState())
                .automaticTransitionFromOpenToHalfOpenEnabled(config.isAutomaticTransitionFromOpenToHalfOpenEnabled())
                .slowCallRateThreshold(config.getSlowCallRateThreshold())
                .failureRateThreshold(config.getFailureRateThreshold())
                .slowCallDurationThreshold(config.getSlowCallDurationThreshold())
                .build();
    }

    @Builder
    public record CircuitBreakerParam(Integer slidingWindowSize,
                               Integer minimumNumberOfCalls,
                               Integer permittedNumberOfCallsInHalfOpenState,
                               Boolean automaticTransitionFromOpenToHalfOpenEnabled,
                               Float slowCallRateThreshold,
                               Float failureRateThreshold,
                               Duration slowCallDurationThreshold) {

    }
}
