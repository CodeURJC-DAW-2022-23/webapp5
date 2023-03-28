#!/bin/bash

docker build -f Dockerfile -t $1 ../
docker push $1
sleep 10000

