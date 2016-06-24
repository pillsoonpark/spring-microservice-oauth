package com.rgm.auth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Secure pages and setup Spring Security login with in-memory users.
 *
 * @author Rob Mills
 * @version 1.1
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Bean
	public static BCryptPasswordEncoder encoder() throws Exception {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {

		auth.inMemoryAuthentication()
				.passwordEncoder(encoder)
				.withUser("setup").password(encoder.encode("setup")).roles("SETUP");

		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.passwordEncoder(encoder)
				.withUser("admin").password(encoder.encode("admin")).roles("ADMIN")
				.and()
				.withUser("user").password(encoder.encode("user")).roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/oauth/**")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/login*","/api*","/api/**")
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