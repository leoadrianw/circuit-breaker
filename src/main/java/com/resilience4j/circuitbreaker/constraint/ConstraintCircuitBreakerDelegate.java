//package com.resilience4j.circuitbreaker.constraint;
//
//import com.resilience4j.circuitbreaker.config.CircuitBreakerProperties;
//import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
//import org.springframework.cloud.client.circuitbreaker.ConfigBuilder;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class ConstraintCircuitBreakerDelegate extends CircuitBreakerFactory<CircuitBreakerProperties, > {
//    private final Resilience4JCircuitBreakerFactory factory;
//
//    public ConstraintCircuitBreakerDelegate(Resilience4JCircuitBreakerFactory factory) {
//        this.factory = factory;
//    }
//    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker()
//
//    @Override
//    public CircuitBreaker create(String id) {
//        return factory.create(id);
//    }
//
//    @Override
//    protected ConfigBuilder configBuilder(String id) {
//        return null;
//    }
//
//    @Override
//    public void configureDefault(Function defaultConfiguration) {
////        defaultConfiguration.apply()
//    }
//}
