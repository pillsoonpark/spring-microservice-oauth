package com.rgm.user;

import com.rgm.Link;
import lombok.Data;

import java.util.Map;

/**
 * A DTO to store the User API response body
 *
 * {
 *   "email" : "rob@email.com",
 *   "enabled" : true,
 *   "_links" : {
 *      "self" : {
 *        "href" : "http://192.168.59.103:8080/api/users/4f6f0f20-d834-40b3-9e8b-0d7c2dfbffa0"
 *      }
 *   }
 * }
 *
 * @author Rob Mills
 * @version 1.0
 * @since
 */
@Data
public class UserApiResponse {
	private String email;
	private boolean enabled;
	private Map<String, Link> _links;

}
