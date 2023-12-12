package com.resilience4j.circuitbreaker.service;

public class CallFallback implements Fallback<String> {
    @Override
    public String call(Throwable throwable) {
        return "Fallback Method";
    }
}
