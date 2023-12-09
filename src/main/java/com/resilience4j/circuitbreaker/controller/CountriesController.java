package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.request.CircuitBreakerRequest;
import com.resilience4j.circuitbreaker.response.CircuitBreakerResponse;
import com.resilience4j.circuitbreaker.service.ChangeConfigService;
import com.resilience4j.circuitbreaker.service.CountriesService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration.CB_COUNTRY_CONFIG;
import static com.resilience4j.circuitbreaker.config.CircuitBreakerConfiguration.COUNTRY_CB_NAME;

@RestController
@RefreshScope
public class CountriesController {
    private final CountriesService countriesService;
    private final ChangeConfigService changeConfigService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CountriesController(CountriesService countriesService,
                               ChangeConfigService changeConfigService,
                               CircuitBreakerRegistry circuitBreakerRegistry) {
        this.countriesService = countriesService;
        this.changeConfigService = changeConfigService;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostMapping("/change-config")
    public ResponseEntity<CircuitBreakerResponse> setCircuitBreakerProperties(@RequestBody CircuitBreakerRequest request) {
        return ResponseEntity.ok(changeConfigService.setCircuitBreakerProperties(request));
    }

    @GetMapping("/countries")
    public List<Object> getCountries() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(COUNTRY_CB_NAME, CB_COUNTRY_CONFIG);

        return Decorators
                .ofSupplier(countriesService::getCountries)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(this::getAlternateMessage)
                .decorate()
                .get();
    }

    public List<Object> getAlternateMessage(Throwable throwable) {
        List<Object> countries = new ArrayList<>();
        countries.add("Countries Service Unavailable");
        return countries;
    }
}
