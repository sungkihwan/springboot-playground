#!/bin/bash

echo "프로젝트 빌드를 시작합니다."

# 운영체제 확인
echo "현재 운영체제는 $OSTYPE 입니다."
if [[ "$OSTYPE" == "darwin"* ]]; then
    # MacOS
    gradle build
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    ./gradlew build
else
    gradlew build
    echo $OSTYPE
fi

sleep 1

echo "Docker 이미지 빌드를 시작합니다."
docker build -t singahu/sscm-service:latest .
echo "Docker 이미지 빌드가 완료되었습니다."
