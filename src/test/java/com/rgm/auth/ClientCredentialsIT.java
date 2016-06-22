package com.rgm.auth;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

/**
 * @author Rob Mills
 * @version 1.0
 * @since 1.2
 */
public class ClientCredentialsIT extends AbstractApiIT {

	// Setup the API endpoint path
	static { RestAssured.basePath = AUTH_URI.TOKEN.uri(); }

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
