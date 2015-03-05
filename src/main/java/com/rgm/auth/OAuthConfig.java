package com.rgm.auth;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Configuration for the OAuth server.
 *
 * Set up a custom persistent JDBC token store that allows other microservices
 * to validate tokens provided tokens.
 *
 * Configure OAuth clients in-memory (for now).
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAuthorizationServer
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

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
	 * Provides a persistent JdbcTokenStore using the DataSource configured in
	 * #dataSource.
	 *
	 * @return the persistent JdbcTokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("app")
					.authorities("ROLE_ADMIN")
					.resourceIds("com.rgm.auth","com.rgm.app")
					.scopes("read", "write")
					.authorizedGrantTypes("client_credentials")
					.secret("password")
				.and()
				.withClient("web")
					.redirectUris("http://robgmills.com/oauth/receive")
					.resourceIds("com.rgm.auth","com.rgm.app")
					.scopes("read")
					.authorizedGrantTypes("implicit");
	}
}