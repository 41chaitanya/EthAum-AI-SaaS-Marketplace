package com.ethaum.credibility_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ethaum.credibility", "com.ethaum.credibility_service"})
@EntityScan(basePackages = "com.ethaum.credibility.model")
@EnableJpaRepositories(basePackages = "com.ethaum.credibility.repository")
public class CredibilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredibilityServiceApplication.class, args);
	}

}
