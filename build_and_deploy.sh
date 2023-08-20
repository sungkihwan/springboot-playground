#!/bin/bash

# 변수 설정
DOCKERFILE_PATH="./Dockerfile"
IMAGE_NAME="singahu/sm-sscm-service:latest"
K8S_CONFIG_PATH="./k8s-scripts/sm-sscm-deployment.yaml"

# Docker 이미지 빌드
echo "Docker 이미지 빌드 중..."
docker build -t $IMAGE_NAME -f $DOCKERFILE_PATH .

## Docker Hub에 이미지 업로드
#echo "Docker Hub에 이미지 업로드 중..."
#docker push $IMAGE_NAME

# 쿠버네티스 디플로이먼트 실행
echo "쿠버네티스 디플로이먼트 실행 중..."
kubectl apply -f $K8S_CONFIG_PATH

