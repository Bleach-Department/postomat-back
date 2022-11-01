#!/bin/bash

sudo ./gradlew regions:publishImageToLocalRegistry
sudo ./gradlew ktor:publishImageToLocalRegistry