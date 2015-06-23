#!/bin/bash
# Handy wrapper script that makes an HTTP POST request to the users API endpoint 
# to create a user.
#
# Ex:
#     ./post-user.sh myusername mypassword

function print_example() {
	echo "Please provide a ${1}!!"
	echo "Ex: ./post-user.sh myusername mypassword"
}

if [ -z "$1" ]; then
	print_example "username"
	exit 1
fi

if [ -z "$2" ]; then
	print_example "password"
	exit 1
fi

json="{\"email\":\"${1}\",\"password\":\"${2}\",\"enabled\":\"true\"}"

curl -v -H "Content-Type: application/json" -X POST -d $json http://`boot2docker ip`:8080/api/users
