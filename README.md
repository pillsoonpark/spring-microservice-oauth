# Spring OAuth Micro-service

This is a basic micro-service based off of common Spring projects to provide an OAuth micro-service to be used to 
secure other micro-service REST APIs.  It works by persisting the OAuth tokens to a shared data store.  Included is 
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

### Client Credentials Flow

The client credentials flow is one of the more basic and less trustworthy flows.  Since the client authentication is 
the only credentials used to gain access to resources, no other user authorization request is necessary.  This flow 
should only be used to grant access to API endpoints that do not contain private user information.

#### Example Request 

    curl -XPOST app:password@`boot2docker ip`:8080/oauth/token\?grant_type=client_credentials


#### Example Response

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Cache-Control: no-store
    Pragma: no-cache

    {"access_token":"cb50edc3-bf94-445c-b9cf-4040dd1901c1","token_type":"bearer","expires_in":43199,"scope":"read 
    write"}

## Change Log
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
- Persistent Users
- Persistent OAuth clients
- Implement more grant type examples
- Customize the authorization page
- Add control of authorized clients for users