# vim:set ft=dockerfile:
FROM java:8
MAINTAINER Rob Mills <me@robgmills.com>
VOLUME /tmp
ADD target/spring-microservice-oauth.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Doauth.db.url=jdbc:postgresql://${DB_PORT_5432_TCP_ADDR}:5432/auth_db", "-Doauth.db.driver-class-name=org.postgresql.Driver", "-Doauth.db.username=auth_user", "-Doauth.db.password=auth_pass", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n", "-jar","/app.jar"]
EXPOSE 8080
EXPOSE 8000