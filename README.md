# Spring OAuth Micro-service

This is a basic micro-service based off of common Spring projects to provide an OAuth micro-service to be used as a security
 gateway to other micro-service REST APIs.  It works by persisting the OAuth tokens to a shared data store.  Included is 
the dependency on the PostgreSQL JDBC driver, which I prefer.  Simply update the dependency if you prefer something 
else.

## To Run
### Build the application
`$ mvn clean package` 

### Build the docker containers
`$ docker-compose build`

### Run everything
`$ docker-compose up`

## How it works
This application serves as an [OAuth2](http://tools.ietf.org/html/rfc6749) provider. A couple of OAuth clients have 
been pre-configured to showcase the different OAuth2 flows. 

### [Authorization Code Grant](https://tools.ietf.org/html/rfc6749#section-4.1)

The authorization code flow is one of the more secure flows specified by the OAuth2 standard.  It should be used to allow
users to grant client applications access to sensitive user data.  Because of this, the authorization code flow is one 
of the more widely used flows.

#### Authorization Code Grant Steps 

0. In a browser, navigate to:
`http://<docker_host>:8080/oauth/authorize?client_id=app&redirect_uri=http://localhost/oauth_callback&response_type=code&scope=read%20write`

    After signing in and approving access for the client `app` you'll be redirected to the url provided as the `redirect_uri` 
    parameter.  (*NOTE: Spring Security OAuth checks the redirect_uri value against the one that is configured for the client 
    and will not provide an authorization code if they do not match.*)  

0. Appended to the end of the uri will be a query string containing a new `code` parameter (e.g. `?code=MFtOwc`).  Exchange 
the authorization code for an access token

        curl -v -X POST --data "code=<authorization_code>&grant_type=authorization_code" http://app:password@<docker_host>:8080/oauth/token

        HTTP/1.1 200 OK
        Content-Type: application/json;charset=UTF-8
        ....
        {"access_token":"ac8c8715-2897-4fdc-a683-e413aa35049b","token_type":"bearer","expires_in":43199,"scope":"read write"}

### [Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4)

The client credentials flow is one of the more basic and less trustworthy flows.  Since the client authentication is 
the only credentials used to gain access to resources, no other user authorization request is necessary.  This flow 
should only be used to grant access to API endpoints that do not contain private user information.

#### Client Credentials Grant Steps

0. Request a client credentials token using the client id and secret:

        curl -v -X POST --data "grant_type=client_credentials" app:password@<docker host>:8080/oauth/token

        HTTP/1.1 200 OK
        Content-Type: application/json;charset=UTF-8
        ....
        {"access_token":"cb50edc3-bf94-445c-b9cf-4040dd1901c1","token_type":"bearer","expires_in":43199,"scope":"read write"}

## Change Log
### 1.3
Features:
- Implement Authorization Code Grant flow
- Persistent users

### 1.2
Features:
- Docker image for PostgreSQL database schema
- Docker image for application

### 1.1
Features:
- Persistent user config

### 1.0
Features:
- In-memory user and OAuth client configuration
- Persistent OAuth tokens

## Road Map (in no particular order)
- Persistent OAuth codes
- Persistent OAuth clients
- Implement more grant type examples
    - Implicit Grant
    - Resource Owner Password Credentials Grant
- Customize the authorization page
- Add control of authorized clients for users
- Use embedded database cluster with flush-to-disk capabilities
- Friendly API error messages and status codes
- Registration/Account management
