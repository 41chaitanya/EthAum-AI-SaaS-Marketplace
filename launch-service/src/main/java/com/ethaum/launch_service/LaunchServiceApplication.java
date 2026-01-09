package com.ethaum.launch_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ethaum.launch", "com.ethaum.launch_service"})
@EntityScan(basePackages = "com.ethaum.launch.model")
@EnableJpaRepositories(basePackages = "com.ethaum.launch.repository")
public class LaunchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaunchServiceApplication.class, args);
	}

}
