package com.resilience4j.circuitbreaker;

import com.resilience4j.circuitbreaker.config.CircuitBreakerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties({CircuitBreakerProperties.class})
@SpringBootApplication
public class CircuitbreakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitbreakerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
