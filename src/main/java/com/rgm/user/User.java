package com.rgm.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * JPA entity representing a user of the system.
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
@Entity
@Table(name="users")
@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class User {

	private static final int MIN_EMAIL_LENGTH = 5;
	private static final int MAX_EMAIL_LENGTH = 128;
	private static final int MAX_PASSWORD_LENGTH = 60;

	@Id
	@Column(name="id", nullable = false, unique = true)
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id = UUID.randomUUID().toString();

	@Size(min = MIN_EMAIL_LENGTH, max = MAX_EMAIL_LENGTH)
	@Pattern(regexp = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$")
	@Column(name="username", nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
	private String email;

	@Size(min = MAX_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
	@Column(name="password", nullable = false, length = MAX_PASSWORD_LENGTH)
	@JsonDeserialize(using = BCryptPasswordDeserializer.class )
	private String password;

	@Column(name="enabled", nullable = false)
	private boolean enabled;
}
