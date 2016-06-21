package com.rgm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Entry point for the Spring Boot application.
 *
 * @author Rob Mills
 * @version 1.1
 * @since 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.rgm.user")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}