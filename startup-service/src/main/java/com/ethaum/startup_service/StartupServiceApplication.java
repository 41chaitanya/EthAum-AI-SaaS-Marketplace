package com.ethaum.startup_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ethaum.startup", "com.ethaum.startup_service"})
@EntityScan(basePackages = "com.ethaum.startup.model")
@EnableJpaRepositories(basePackages = "com.ethaum.startup.repository")
public class StartupServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartupServiceApplication.class, args);
	}

}
