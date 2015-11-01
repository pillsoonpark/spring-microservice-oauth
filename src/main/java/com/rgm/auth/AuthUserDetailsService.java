package com.rgm.auth;

import com.rgm.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<com.rgm.user.User> users = userRepository.findByEmail(username);

		if (users.isEmpty()) {
			throw new UsernameNotFoundException("Username " + username + " not found");
		}

		else if (users.size() > 1) {
			throw new UsernameNotFoundException("Duplicate users for username " + username + " found!");
		}

		return new User(username, "password", getGrantedAuthorities(username));
	}

	// TODO: replace with persistent roles
	private Collection<? extends GrantedAuthority> getGrantedAuthorities(String
			username) {
		Collection<? extends GrantedAuthority> authorities;
		if (username.equals("admin")) {
			authorities = Arrays.asList(() -> "ROLE_ADMIN", () -> "ROLE_BASIC");
		} else {
			authorities = Arrays.asList(() -> "ROLE_BASIC");
		}
		return authorities;
	}
}
