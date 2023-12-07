package com.resilience4j.circuitbreaker.domain;

import lombok.Builder;

@Builder
public record Country (
        Long id,
        String name) {
}
