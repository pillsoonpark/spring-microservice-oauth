package com.rgm;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Configuration for the application's data source(s).
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
@Configuration
public class AppDbConfig {

	/**
	 * Configures the data source using the application properties
	 * for the OAuth database url and driver class name.
	 *
	 * @return the fully configured DataSource
	 */
	@Bean
	@ConfigurationProperties(prefix = "oauth.db")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * Set up all the required database schemas.
	 */
	@PostConstruct
	public void setUpDatasource() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		jdbcTemplate.execute(CREATE_OAUTH_ACCESS_TOKEN_SQL);
		jdbcTemplate.execute(CREATE_USERS_SQL);
		jdbcTemplate.execute(CREATE_AUTHORITIES_SQL);
		jdbcTemplate.execute(DROP_IX_AUTH_USERNAME_SQL);
		jdbcTemplate.execute(CREATE_IX_AUTH_USERNAME_SQL);
	}

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
			"ON authorities (username,authority);";
}
