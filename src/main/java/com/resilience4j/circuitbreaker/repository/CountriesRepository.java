package com.resilience4j.circuitbreaker.repository;

import com.resilience4j.circuitbreaker.domain.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountriesRepository {
    public List<Country> findAll() {
        Country indo = Country.builder()
                .id(1L)
                .name("Indonesia")
                .build();
        Country japan = Country.builder()
                .id(2L)
                .name("Japan")
                .build();
        Country usa = Country.builder()
                .id(3L)
                .name("USA")
                .build();

        return List.of(indo, japan, usa);
    }
}
