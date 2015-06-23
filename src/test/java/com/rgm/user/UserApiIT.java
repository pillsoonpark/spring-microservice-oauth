package com.rgm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.rgm.AbstractResourceApiIT;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

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

	@Override
	public void crud() throws Exception {

		// Create
		String createUserJson = generateJsonBody(USER_JSON_COMPLETE, "joe@something.com", "secretPassword123");

		// @formatter:off
		String location =
				given()
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.body(createUserJson)
				.when()
						.post()
				.then()
						.statusCode(HttpStatus.CREATED.value())
				.extract()
						.header(HEADER_LOCATION);
		// @formatter:on

		assertTrue("Invalid location url on create user", isValidLocationUrl(location));

		// Read
		// @formatter:off
		String responseBodyString =
				given()
						.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
						.get(location)
				.then()
						.statusCode(HttpStatus.OK.value())
				.extract()
						.body().asString();
		// @formatter:on

		ObjectMapper mapper = new ObjectMapper();
		UserApiResponse apiResponse = mapper.readValue(responseBodyString, UserApiResponse.class);
		assertThat("Email address does not match created user", apiResponse.getEmail(), equalTo("joe@something.com"));
		assertTrue("User should be enabled", apiResponse.isEnabled());
		assertFalse("Password value should not be present", responseBodyString.contains("\"password\" : \""));

		// Update
		String updateUserJson = generateJsonBody(USER_JSON_NO_PASSWORD, "joe@something.com");

		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(updateUserJson)
		.when()
				.patch(location)
		.then()
				.statusCode(HttpStatus.NO_CONTENT.value())
		.extract()
				.header(HEADER_LOCATION);
		// @formatter:on

		// Replace
		String replaceUserJson = generateJsonBody(USER_JSON_COMPLETE, "replace@email.com", "replacePass");
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(replaceUserJson)
		.when()
				.put(location)
		.then()
				.statusCode(HttpStatus.NO_CONTENT.value())
		.extract()
				.header(HEADER_LOCATION);
		// @formatter:on

		// Delete
		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		.when()
				.delete(location)
		.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
		// @formatter:on

		// @formatter:off
		given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		.when()
				.get(location)
		.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
		// @formatter:on
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

	@Ignore("Currently no different the happy path create")
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
