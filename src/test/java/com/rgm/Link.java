package com.rgm;

import lombok.Data;

import java.net.URI;

/**
 * A DTO to store the HATEOAS "_links" response property returned with all
 * HTTP GET API operations.
 *
 * Example:
 * <code>
 * {
 *   [some entity properties]
 *   "_links" : {
 *     "self" : {
 *     "href" : "http://192.168.59.103:8080/api/users/4f6f0f20-d834-40b3-9e8b-0d7c2dfbffa0"
 *   }
 * }
 * </code>
 *
 * @author Rob Mills
 * @version 1.0
 * @since
 */
@Data
public class Link {
	private URI href;
}
