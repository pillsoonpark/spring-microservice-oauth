#!/bin/bash
docker rm auth_app_1; \
docker rmi auth_app; \
mvn clean package && \
docker-compose build app && \
docker-compose up
