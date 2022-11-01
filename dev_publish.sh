#!/bin/bash

sudo ./gradlew regions:publishImageToLocalRegistry
sudo ./gradlew ktor:publishImageToLocalRegistry

sudo docker-compose up
sudo docker-compose down

sudo docker rmi -f postomat-back-ktor:0.0.1
sudo docker rmi -f postomat-back-regions:0.0.1
