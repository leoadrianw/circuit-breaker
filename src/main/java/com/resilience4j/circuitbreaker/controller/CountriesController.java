package com.resilience4j.circuitbreaker.controller;

import com.resilience4j.circuitbreaker.annotation.CustomCircuitBreaker;
import com.resilience4j.circuitbreaker.service.CallFallback;
import com.resilience4j.circuitbreaker.service.CountriesService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
public class CountriesController {
    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }


    @GetMapping("/countries")
    @CustomCircuitBreaker(fallbackMethod = CallFallback.class)
    public List<Object> getCountries() {

        return countriesService.getCountries();
    }

    public List<Object> getAlternateMessage(Throwable throwable) {
        List<Object> countries = new ArrayList<>();
        countries.add("Countries Service Unavailable");
        return countries;
    }
}
