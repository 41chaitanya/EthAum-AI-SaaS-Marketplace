package com.ethaum.review_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ethaum.review", "com.ethaum.review_service"})
@EntityScan(basePackages = "com.ethaum.review.model")
@EnableJpaRepositories(basePackages = "com.ethaum.review.repository")
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

}
