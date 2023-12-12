package com.resilience4j.circuitbreaker.service;

public interface Fallback<T> {
    T call(Throwable throwable);
}
