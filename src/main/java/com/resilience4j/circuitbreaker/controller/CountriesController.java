package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.request.CircuitBreakerRequest;
import com.resilience4j.circuitbreaker.response.CircuitBreakerResponse;
import com.resilience4j.circuitbreaker.service.ChangeConfigService;
import com.resilience4j.circuitbreaker.service.CountriesService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RestController
public class CountriesController {
    private final CountriesService countriesService;
    private final ChangeConfigService changeConfigService;
    private final CircuitBreaker circuitBreaker;

    public CountriesController(CountriesService countriesService, ChangeConfigService changeConfigService, CircuitBreaker circuitBreaker) {
        this.countriesService = countriesService;
        this.changeConfigService = changeConfigService;
        this.circuitBreaker = circuitBreaker;
    }

    @PostMapping("/change-config")
    public ResponseEntity<CircuitBreakerResponse> setCircuitBreakerProperties(@RequestBody CircuitBreakerRequest request) {
        return ResponseEntity.ok(changeConfigService.setCircuitBreakerProperties(request));
    }

    @GetMapping("/countries")
    public List<Object> getCountries() throws Exception {
        Supplier<List<Object>> countrySupplier = () -> countriesService.getCountries();
        Supplier<List<Object>> decoratedSupplier = Decorators
                .ofSupplier(countrySupplier)
                        .withCircuitBreaker(circuitBreaker)
                                .withFallback(this::getAlternateMessage)
                                        .decorate();
        return decoratedSupplier.get();
    }
    public List<Object> getAlternateMessage(Throwable throwable) {
        List<Object> countries = new ArrayList<>();
        countries.add("Countries Service Unavailable");
        return countries;
    }
}
