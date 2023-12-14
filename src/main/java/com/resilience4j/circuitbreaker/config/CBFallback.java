package com.resilience4j.circuitbreaker.config;

import io.github.resilience4j.core.functions.CheckedFunction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CBFallback implements CheckedFunction<Throwable, List<Object>> {

    @Override
    public List<Object> apply(Throwable throwable) throws Throwable {
        List<Object> newList = new ArrayList<>();
        newList.add("Countries service is currently unavailable");
        return newList;
    }
}
