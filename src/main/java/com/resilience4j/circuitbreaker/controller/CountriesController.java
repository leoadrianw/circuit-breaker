package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.config.CBFallback;
import com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration;
import com.resilience4j.circuitbreaker.config.DelegateCircuitBreaker;
import com.resilience4j.circuitbreaker.service.CountriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration.CB_COUNTRY_CONFIG;
import static com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration.COUNTRY_CB_NAME;

@RestController
public class CountriesController {
    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/countries")
    @DelegateCircuitBreaker(
            name = COUNTRY_CB_NAME,
            serviceCode = "30",
            configName = CB_COUNTRY_CONFIG,
            configuration = CircuitBreakerConfiguration.class,
            fallback = CBFallback.class
    )
    public List<Object> getCountries() {
        return countriesService.getCountries();
    }
}
