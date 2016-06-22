package com.rgm.auth;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.config.RedirectConfig.*;
import static junit.framework.TestCase.*;
import static org.hamcrest.Matchers.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.response.Response;
import org.junit.Test;

/**
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
public class AuthorizationCodeIT extends AbstractApiIT {

	@Test
	public void authorizationCodeValid() {
		RestAssured.urlEncodingEnabled = false;
		SessionFilter sessionFilter = new SessionFilter();

		// get login page w/client information

		given()
				.auth()
				.form("admin", "admin", new FormAuthConfig("/login", "email", "password"))
				.filter(sessionFilter)
				.param("client_id", "app")
				.param("redirect_uri", "http://localhost/oauth_callback")
				.param("response_type", "code")
				.param("scope", "read%20write")
				.when()
				.get(AUTH_URI.AUTHORIZE.uri())
				.then()
				.statusCode(200)
		// should be validating the input here, but the thymeleaf template for the confirmation page has an element
		// <input ... checked /> which should be <input ... checked="checked" /> that's causing the SAX Parser to
		// break.
			/*.body(
					hasXPath("//form[@id=\"confirmationForm\"]"),
					hasXPath("//input[@type=\"password\" and @name=\"password\"]") )*/;

		// authorize client access
		Response response =
				given()
						// disable redirects because the callback url doesn't actually exist
						.config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
						.formParam("user_oauth_approval", "true")
						.formParam("scope.read", "true")
						.formParam("scope.write", "true")
						.formParam("authorize", "Authorize")
						.filter(sessionFilter)
						.when()
						.post(AUTH_URI.AUTHORIZE.uri())
						.then()
						.extract()
						.response();

		String redirectLocation = response.getHeader("Location");
		Pattern pattern = Pattern.compile("^.*\\?code=(.*)$");
		Matcher matcher = pattern.matcher(redirectLocation);
		boolean hasCode = matcher.find();
		assertTrue("redirect location should contain authorization code", hasCode);

		if( hasCode ) {
			String code = matcher.group(1);

			// exchange code for token
			given()
					.formParam("code", code)
					.formParam("grant_type", "authorization_code")
					.formParam("redirect_uri", "http://localhost/oauth_callback")
					.auth()
					.preemptive()
					.basic("app", "password")
					.when()
					.post(AUTH_URI.TOKEN.uri())
					.then()
					.statusCode(200)
					.body("access_token", not(isEmptyOrNullString()))
					.body("token_type", equalToIgnoringWhiteSpace("bearer"))
					.body("scope", equalToIgnoringWhiteSpace("read write"));
		}
	}
}
