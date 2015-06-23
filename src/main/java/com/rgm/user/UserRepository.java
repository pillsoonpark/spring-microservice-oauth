package com.rgm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * JPA Repository and queries for {@link User} entities
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
public interface UserRepository extends CrudRepository<User, String> {
	@PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
	List<User> findByEmail(@Param("email") String email);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Page<User> findAll(Pageable pageable);
}
