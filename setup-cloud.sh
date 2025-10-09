#!/bin/bash

echo "🚀 Kafka Flash Sale System - Cloud Setup"
echo "========================================"

# Java 17 확인
echo "📋 Checking Java version..."
java -version

# Kafka 설치 (Ubuntu/Debian)
echo "📦 Installing Kafka..."
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk kafka

# Kafka 서비스 시작
echo "🔄 Starting Kafka services..."
sudo systemctl start zookeeper
sudo systemctl start kafka

# 잠시 대기
sleep 10

# 토픽 생성
echo "📝 Creating Kafka topic..."
kafka-topics --create --topic order-requests --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# 토픽 확인
echo "✅ Verifying topic creation..."
kafka-topics --list --bootstrap-server localhost:9092

# 애플리케이션 실행
echo "🎯 Starting Spring Boot application..."
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew bootRun
