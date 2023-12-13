package com.resilience4j.circuitbreaker.service;

import java.util.ArrayList;
import java.util.List;

public class CallFallback extends FallbackClass {
    public List<Object> getFallback(Throwable throwable) {
        List<Object> newList = new ArrayList<>();
        newList.add("Countries service is currently unavailable");
        return newList;
    }
}
