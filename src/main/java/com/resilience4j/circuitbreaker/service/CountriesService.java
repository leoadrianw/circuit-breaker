package com.resilience4j.circuitbreaker.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CountriesService {
    public static final String COUNTRIES_API = "https://restcountries.com/v3.1/all";
    private final RestTemplate restTemplate;

    public CountriesService(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    public List<Object> getCountries() {
        ResponseEntity<List<Object>> exchange = restTemplate.exchange(
                COUNTRIES_API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        List<Object> countries = exchange.getBody();

        return Optional.ofNullable(countries)
                .map(list -> list.subList(1, 10))
                .orElse(Collections.emptyList());
    }
}
