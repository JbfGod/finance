#!/bin/bash

set -e -u

root_dir="$(cd $(dirname $0)/../ && pwd)"

echo 'Package files into tar.gz file'
cd "${root_dir}"
tar -cvzf app.tar.gz "build/libs/" "Dockerfile" "docker-compose.yml" "startDocker.sh"

echo 'Package done.'
