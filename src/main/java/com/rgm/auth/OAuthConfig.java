package com.rgm.auth;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

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

	@Autowired
	private DataSource dataSource;

	/**
	 * Provides a persistent JdbcTokenStore using the DataSource configured in
	 * #dataSource.
	 *
	 * @return the persistent JdbcTokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("app")
					.authorities("CLIENT")
					.resourceIds("com.rgm.auth","com.rgm.app")
					.scopes("read", "write")
					.authorizedGrantTypes("client_credentials", "authorization_code")
					.secret("password")
					.redirectUris("http://localhost/oauth_callback");
	}
}
