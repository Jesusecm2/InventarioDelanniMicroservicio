package com.microservices.delanni.inventory.app;

import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;



public class AppConfig {

	
	
	/*@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultcustomizer(){
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10)
							.failureRateThreshold(50)
							.waitDurationInOpenState(Duration.ofSeconds(10L))
							.permittedNumberOfCallsInHalfOpenState(5)
							.slowCallRateThreshold(50L)
							.slowCallDurationThreshold(Duration.ofSeconds(2L))
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(15L)).build())
					.build();
		});
		
	}*/
}
