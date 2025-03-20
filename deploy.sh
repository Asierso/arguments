#!/bin/bash
echo Arguments deployer by Asier
echo Deploying Arguments...
docker build -t asier/arguments-backend .
docker-compose up
echo Stopped Arguments
#Debugging: docker-compose down -v