package com.rgm.user;

import com.jayway.restassured.RestAssured;
import com.rgm.AbstractResourceApiIT;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

/**
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
public class UserApiIT extends AbstractResourceApiIT {

	static { RestAssured.basePath = "/api/users"; }

	private static final String USER_JSON_COMPLETE = "{"
			+ "\"email\":\"%s\","
			+ "\"password\":\"%s\""
		+ "}";

	private static final String USER_JSON_NO_EMAIL = "{"
			+ "\"password\":\"%s\""
		+ "}";

	private static final String USER_JSON_NO_PASSWORD = "{"
			+ "\"email\":\"%s\""
		+ "}";

	@Test
	public void createValidUser() throws Exception {
		// @formatter:off
		String location =
				given()
						.contentType("application/json") // TODO: replace with enum value
						.body(generateJsonBody(USER_JSON_COMPLETE, "joe@something.com", "secretPassword123", "true"))
				.when()
						.post()
				.then()
						.statusCode(HttpStatus.CREATED.value())
				.extract()
						.header(HEADER_LOCATION);
		// @formatter:on
		assertTrue("Invalid location url on create user", isValidLocationUrl(location));
	}

	@Test
	public void createInvalidUserNoEmailValue() throws Exception {
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(generateJsonBody(USER_JSON_COMPLETE, "", "secretPassword123"))
		.when()
				.post()
		.then()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		// @formatter:on
	}

	@Test
	public void createInvalidUserInvalidEmailValue() throws Exception {
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(generateJsonBody(USER_JSON_COMPLETE, "notanemail", "secretPassword123"))
		.when()
				.post()
		.then()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		// @formatter:on
	}

	@Test
	public void createInvalidUserNoEmail() throws Exception {
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(generateJsonBody(USER_JSON_NO_EMAIL, "secretPassword123"))
		.when()
				.post()
		.then()
				.statusCode(HttpStatus.CONFLICT.value()); // TODO: Why does this throw a 409?
		// @formatter:on
	}

	@Test
	public void createInvalidUserNoPasswordValue() throws Exception {
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(generateJsonBody(USER_JSON_COMPLETE, "rob@email.com", ""))
		.when()
				.post()
		.then()
				.statusCode(HttpStatus.CREATED.value()); // TODO: don't allow this!!
		// @formatter:on
	}

	@Test
	public void createInvalidUserNoPassword() throws Exception {
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(generateJsonBody(USER_JSON_NO_PASSWORD, "robnopw@email.test"))
		.when()
				.post()
		.then()
				.statusCode(HttpStatus.CONFLICT.value()); // TODO: why does this throw a 409?
		// @formatter:on
	}
}
