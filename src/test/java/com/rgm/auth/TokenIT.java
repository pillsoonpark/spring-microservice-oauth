package com.rgm.auth;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * @author Rob Mills
 * @version 1.0
 * @since 1.2
 */
public class TokenIT extends AbstractApiIT {

	// Setup the API endpoint path
	static { RestAssured.basePath = "/oauth/token"; }

	@Test
	public void clientCredentialsNoAuth() {
		given().
				param("grant_type", "client_credentials").
		when().
				post().
		then().
				statusCode(401);
	}

	@Test
	public void clientCredentialsInvalid() {
		given().
				auth().preemptive().basic("doesnotexist", "doesnotexist").
				param("grant_type", "client_credentials").
		when().
				post().
		then().
				statusCode(401);
	}

	@Test
	public void clientCredentialsValid() {
		given().
				auth().preemptive().basic("app", "password").
				param("grant_type", "client_credentials").
		when().
				post().
		then().
				statusCode(200).
				body("token_type", equalTo("bearer")).
				body("scope", containsString("read")).
				body("scope", containsString("write")).
				body("access_token", notNullValue());
	}
}
