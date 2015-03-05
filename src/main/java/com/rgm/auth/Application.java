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
 * @version 1.1
 * @since 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {

	private static final String CREATE_OAUTH_ACCESS_TOKEN_SQL = "CREATE TABLE IF NOT EXISTS oauth_access_token ("+
			"token_id VARCHAR(256) NOT NULL PRIMARY KEY,"+
			"token BYTEA,"+
			"authentication_id VARCHAR(256),"+
			"user_name VARCHAR(256),"+
			"client_id VARCHAR(256),"+
			"authentication BYTEA,"+
			"refresh_token VARCHAR(256)"+
			");";

	private static final String CREATE_USERS_SQL = "CREATE TABLE IF NOT EXISTS users ("+
			"username VARCHAR(50) NOT NULL PRIMARY KEY,"+
			"password VARCHAR(60) NOT NULL,"+
			"enabled BOOLEAN NOT NULL DEFAULT TRUE"+
			");";

	private static final String CREATE_AUTHORITIES_SQL = "CREATE TABLE IF NOT EXISTS authorities ("+
			"username VARCHAR(50) NOT NULL,"+
			"authority VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',"+
			"CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)"+
			");";

	private static final String DROP_IX_AUTH_USERNAME_SQL = "DROP INDEX IF EXISTS ix_auth_username;";

	private static final String CREATE_IX_AUTH_USERNAME_SQL = "CREATE UNIQUE INDEX ix_auth_username "+
			"on authorities (username,authority);";

	@Autowired
	private DataSource dataSource;

	/**
	 * Set up all the required database schemas.
	 */
	@PostConstruct
	public void setUpDatasource() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute(CREATE_OAUTH_ACCESS_TOKEN_SQL);
		jdbcTemplate.execute(CREATE_USERS_SQL);
		jdbcTemplate.execute(CREATE_AUTHORITIES_SQL);
		jdbcTemplate.execute(DROP_IX_AUTH_USERNAME_SQL);
		jdbcTemplate.execute(CREATE_IX_AUTH_USERNAME_SQL);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}