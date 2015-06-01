# Spring OAuth Micro-service

This is a basic micro-service based off of common Spring projects to provide an
OAuth micro-service to be used to secure other micro-service REST APIs.  It
works by persisting the OAuth tokens to a shared data store.  Included is the
dependency on the PostgreSQL JDBC driver, which I prefer.  Simply update the
dependency if you prefer something else.

## To Run
### Build the application
`$ mvn clean package` 

### Build the docker containers
`$ docker-compose build`

### Run everything
`$ docker-compose up`


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