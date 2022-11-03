#!/bin/bash

sudo ./gradlew regions:publishImageToLocalRegistry
sudo ./gradlew ktor:publishImageToLocalRegistry
sudo ./gradlew postomat:publishImageToLocalRegistry