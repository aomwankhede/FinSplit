#!/bin/bash
set -e

# Start MySQL in the background
service mysql start

# Optional: wait for MySQL to fully initialize
sleep 5

mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root'; FLUSH PRIVILEGES;"

# Initialize database if needed
mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};"

# Run your Spring Boot application
java -jar target/*.jar
