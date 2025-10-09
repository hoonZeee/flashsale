# 🌐 클라우드 배포 가이드

## 🚀 GitHub Codespaces (추천)

### 1. Codespaces 생성
1. https://github.com/hoonZeee/flashsale/tree/jinho 접속
2. "Code" → "Codespaces" → "Create codespace on jinho"
3. Codespace가 생성될 때까지 대기 (약 2-3분)

### 2. 자동 설정 실행
```bash
# Codespace 터미널에서 실행
./setup-cloud.sh
```

### 3. 수동 설정 (필요시)
```bash
# Java 17 확인
java -version

# Kafka 설치
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk kafka

# Kafka 서비스 시작
sudo systemctl start zookeeper
sudo systemctl start kafka

# 토픽 생성
kafka-topics --create --topic order-requests --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# 애플리케이션 실행
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew bootRun
```

### 4. 성능 테스트
```bash
# 기본 테스트
ab -n 1000 -c 10 -p order.json -T application/json http://localhost:8081/order-kafka

# 중간 부하 테스트
ab -n 10000 -c 100 -p order.json -T application/json http://localhost:8081/order-kafka

# 고부하 테스트
ab -n 100000 -c 200 -p order.json -T application/json http://localhost:8081/order-kafka
```

## ☁️ AWS EC2

### 1. 인스턴스 생성
- **타입**: t3.medium (2 vCPU, 4GB RAM)
- **OS**: Ubuntu 22.04 LTS
- **보안 그룹**: SSH (22), HTTP (8081), Kafka (9092)

### 2. 연결 및 설정
```bash
# EC2 인스턴스에 SSH 연결
ssh -i your-key.pem ubuntu@your-ec2-ip

# 코드 클론
git clone https://github.com/hoonZeee/flashsale.git
cd flashsale
git checkout jinho

# 설정 실행
./setup-cloud.sh
```

## 🔧 Google Cloud Platform

### 1. 인스턴스 생성
- **머신 타입**: e2-medium (2 vCPU, 4GB RAM)
- **OS**: Ubuntu 22.04 LTS
- **방화벽**: HTTP 트래픽 허용

### 2. 연결 및 설정
```bash
# GCP 인스턴스에 SSH 연결
gcloud compute ssh your-instance-name --zone=your-zone

# 코드 클론 및 설정
git clone https://github.com/hoonZeee/flashsale.git
cd flashsale
git checkout jinho
./setup-cloud.sh
```

## 📊 예상 성능

| 환경 | 메모리 | CPU | 예상 처리량 | 10만건 처리 시간 |
|------|--------|-----|-------------|------------------|
| **로컬** | 8GB | 4코어 | 2,600 req/sec | 75초 |
| **Codespaces** | 4GB | 2코어 | 5,000+ req/sec | 20초 |
| **AWS EC2** | 4GB | 2코어 | 8,000+ req/sec | 12초 |
| **GCP** | 4GB | 2코어 | 8,000+ req/sec | 12초 |

## 🎯 권장사항

1. **학습/테스트**: GitHub Codespaces
2. **프로덕션**: AWS EC2 또는 GCP
3. **대용량 테스트**: AWS EC2 (t3.large 이상)

## 🔍 문제 해결

### Kafka 연결 오류
```bash
# Kafka 상태 확인
sudo systemctl status kafka

# 로그 확인
sudo journalctl -u kafka -f
```

### 메모리 부족
```bash
# 메모리 사용량 확인
free -h

# Java 힙 메모리 조정
export JAVA_OPTS="-Xmx2g -Xms1g"
./gradlew bootRun
```

### 포트 충돌
```bash
# 포트 사용 확인
sudo netstat -tlnp | grep :8081
sudo netstat -tlnp | grep :9092
```
