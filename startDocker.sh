#!/usr/bin/env bash

DIR="$( cd "$( dirname $0 )" && pwd )"
cd $DIR

set -e -u
./gradlew clean build -x test

docker-compose stop
sleep 10
docker-compose rm -f
docker-compose build

UNUSEIMAGES=$(docker images --filter "dangling=true" -q --no-trunc)
if [ "$UNUSEIMAGES" != "" ]; then
    docker rmi $UNUSEIMAGES
fi
docker-compose up -d
