package com.resilience4j.circuitbreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CBFallback implements Callable<List<Object>> {
    @Override
    public List<Object> call() throws Exception {
        List<Object> countries = new ArrayList<>();
        countries.add("Countries Service Unavailable");
        return countries;
    }
}
