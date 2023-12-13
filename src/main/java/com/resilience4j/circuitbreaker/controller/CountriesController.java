package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.annotation.CustomCircuitBreaker;
import com.resilience4j.circuitbreaker.config.CircuitBreakerProperties;
import com.resilience4j.circuitbreaker.service.CallFallback;
import com.resilience4j.circuitbreaker.service.CountriesService;
import lombok.Builder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
public class CountriesController {
    private final CircuitBreakerProperties properties;
    private final CountriesService countriesService;

    public CountriesController(CircuitBreakerProperties properties, CountriesService countriesService) {
        this.properties = properties;
        this.countriesService = countriesService;
    }

    @GetMapping("/countries")
    @CustomCircuitBreaker(fallbackMethod = CallFallback.class)
    public List<Object> getCountries() {

        return countriesService.getCountries();
    }

    @GetMapping("/update-properties")
    public List<Object> CircuitBreakerParam() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(properties.getSlidingWindowSize());
        objectList.add(properties.getFailureRateThreshold());
        objectList.add(properties.getSlowCallRateThreshold());
        return objectList;
    }
}
