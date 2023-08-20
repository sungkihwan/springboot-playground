#!/bin/bash

echo "Docker compose로 배포를 시작합니다."
docker compose -f compose/docker-compose-dev.yml up -d