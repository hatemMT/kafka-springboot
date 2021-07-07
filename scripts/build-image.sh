#!/usr/bin/env bash
mvn clean package

docker build -t emp-app-img .
