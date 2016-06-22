package com.rgm.auth;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Sets the host name and port for the API tests
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.2
 */
public abstract class AbstractApiIT {

	// Common place to store the API URIs and make available for tests
	protected enum AUTH_URI {
		LOGIN("/login"),
		TOKEN("/oauth/token"),
		AUTHORIZE("/oauth/authorize");

		private final String uri;

		AUTH_URI(String uri) {
			this.uri = uri;
		}

		public String uri() {
			return uri;
		}
	};

	@BeforeClass
	public static void setupApiHostAndPort() {
		System.out.println("auth.test.host=" + System.getProperty("auth.test.host"));
		RestAssured.baseURI = "http://" + System.getProperty("auth.test.host");
		System.out.println("auth.test.port=" + System.getProperty("auth.test.port"));
		RestAssured.port =  Integer.parseInt(System.getProperty("auth.test.port"));
	}

	@AfterClass
	public static void resetBasePath() {
		reset();
	}
}
