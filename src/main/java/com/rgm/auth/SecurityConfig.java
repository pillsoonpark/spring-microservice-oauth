package com.rgm.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Secure pages and setup Spring Security login with in-memory users.
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").roles("ADMIN").password("password")
				.and()
				.withUser("rob").roles("BASIC").password("password");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/login*")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/**")
				.authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.loginProcessingUrl("/login")
				.and()
			.logout()
				.logoutUrl("/logout")
				.and()
			.csrf() // temporarily disable CSRF
				.disable();
	}
}