package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.CBConfiguration;
import com.resilience4j.circuitbreaker.CBFallback;
import com.resilience4j.circuitbreaker.config.CircuitBreakerProperties;
import com.resilience4j.circuitbreaker.constraint.annotation.DelegateCircuitBreaker;
import com.resilience4j.circuitbreaker.service.CountriesService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
public class CountriesController {
    public static final String CB_COUNTRY_CONFIG = "countriesService";
    public static final String COUNTRY_CB_NAME = "countries-service";
    private final CountriesService countriesService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final CircuitBreakerProperties circuitBreakerProperties;

    public CountriesController(CountriesService countriesService,
                               CircuitBreakerRegistry circuitBreakerRegistry,
                               CircuitBreakerProperties circuitBreakerProperties) {
        this.countriesService = countriesService;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.circuitBreakerProperties = circuitBreakerProperties;
    }

    @GetMapping("/countries")
    @DelegateCircuitBreaker(
            name = COUNTRY_CB_NAME,
            serviceCode = "30",
            configName = CB_COUNTRY_CONFIG,
            configuration = CBConfiguration.class,
            fallback = CBFallback.class
//            ignoreException = {
//                    RuntimeException.class,
//                    IllegalStateException.class
//            }
    )
    public List<Object> getCountries() {
//
//        CircuitBreakerConfig config = CircuitBreakerConfig.from(circuitBreakerConfiguration.getConfig())
//                .slidingWindow(circuitBreakerProperties.slidingWindowSize(), circuitBreakerProperties.minimumNumberOfCalls(), CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
//                .failureRateThreshold(circuitBreakerProperties.failureRateThreshold())
//                .slowCallRateThreshold(circuitBreakerProperties.slowCallRateThreshold())
//                .build();
        return countriesService.getCountries();
//        return Decorators
//                .ofSupplier(countriesService::getCountries)
//                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker(COUNTRY_CB_NAME, CB_COUNTRY_CONFIG))
//                .withFallback(this::getAlternateMessage)
//                .decorate()
//                .get();
    }
//
//    public List<Object> getAlternateMessage(Throwable throwable) {
//        List<Object> countries = new ArrayList<>();
//        countries.add("Countries Service Unavailable");
//        return countries;
//    }
}
