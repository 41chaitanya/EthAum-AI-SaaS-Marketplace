package com.ethaum.deal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ethaum.deal", "com.ethaum.deal_service"})
@EntityScan(basePackages = "com.ethaum.deal.model")
@EnableJpaRepositories(basePackages = "com.ethaum.deal.repository")
public class DealServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealServiceApplication.class, args);
	}

}
