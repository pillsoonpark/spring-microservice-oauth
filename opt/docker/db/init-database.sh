#!/bin/bash

: ${DB_USER:=auth_user}
: ${DB_PASSWORD:=auth_pass}
: ${DB_NAME:=auth_db}

{ gosu postgres postgres --single -jE <<-EOSQL
    CREATE USER "$DB_USER" WITH PASSWORD '$DB_PASSWORD';
EOSQL
} && { gosu postgres postgres --single -jE <<-EOSQL
    CREATE DATABASE "$DB_NAME";
EOSQL
}