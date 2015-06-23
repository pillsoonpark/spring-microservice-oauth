package com.rgm;

/**
 * Common functionality for all resource-based API tests
 *
 * @author Rob Mills
 * @version 1.0
 * @since 1.3
 */
public abstract class AbstractResourceApiIT extends AbstractApiIT {

	protected static final String HEADER_LOCATION = "Location";
	protected static final String REGEX_LOCATION_URL =
			"http(s)?://((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(\\w)+)(:\\d{2,6})?([/\\w\\d]+)+/"
					+ "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})";

	protected static boolean isValidLocationUrl(String location) {
		return location.matches(REGEX_LOCATION_URL);
	}

	/**
	 * Replace JSON string template with the provided args.
	 *
	 * @param jsonBody
	 * @param args
	 * @return
	 */
	protected static String generateJsonBody(String jsonBody, String... args) {
		return String.format(jsonBody, args);
	}
}
