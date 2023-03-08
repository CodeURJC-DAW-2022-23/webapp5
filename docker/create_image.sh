#!/bin/bash

docker build -f Dockerfile -t $1 ../backend
docker push $1