package com.resilience4j.circuitbreaker.repository;

import com.resilience4j.circuitbreaker.domain.CbConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircuitBreakerRepository extends JpaRepository<CbConfig, Long> {
}
