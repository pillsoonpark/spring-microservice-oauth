package com.rgm.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Entry point for the Spring Boot application.
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {

	private static final String CREATE_OAUTH_ACCESS_TOKEN_SQL = "create table if not exists oauth_access_token ("+
			"token_id VARCHAR(256),"+
			"token BYTEA,"+
			"authentication_id VARCHAR(256),"+
			"user_name VARCHAR(256),"+
			"client_id VARCHAR(256),"+
			"authentication BYTEA,"+
			"refresh_token VARCHAR(256)"+
			");";

	private static final String DELETE_TOKENS_SQL = "delete from oauth_access_token";

	@Autowired
	private DataSource dataSource;

	/**
	 * Sets up the token data store and clear any old tokens after
	 * application startup.
	 */
	@PostConstruct
	public void setUpTokenDatasource() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute(CREATE_OAUTH_ACCESS_TOKEN_SQL);
		jdbcTemplate.execute(DELETE_TOKENS_SQL);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}