package com.resilience4j.circuitbreaker.service;

import com.resilience4j.circuitbreaker.properties.CircuitBreakerProperties;
import com.resilience4j.circuitbreaker.repository.CountriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CountriesService {
    private final CountriesRepository countriesRepository;
    private final RestTemplate restTemplate;
    private final CircuitBreakerProperties circuitBreakerProperties;

    public CountriesService(CountriesRepository countriesRepository, RestTemplate restTemplate, CircuitBreakerProperties circuitBreakerProperties) {
        this.countriesRepository = countriesRepository;
        this.restTemplate = restTemplate;
        this.circuitBreakerProperties = circuitBreakerProperties;
    }

    public List<Object> getCountries() {
        Object[] countries = null;
        countries = restTemplate.getForObject("https://restcountries.com/v3.1/all", Object[].class);
        return Arrays.stream(countries).toList().subList(1, 10);
    }
}
