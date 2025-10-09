# Flash Sale 시스템 - 대용량 트래픽 성능 개선 실습

이 프로젝트는 한정판 주문 폭주 상황을 시뮬레이션하기 위한 스프링 부트 실습 예제입니다.
**Kafka 메시지 큐 시스템을 도입하여 대용량 트래픽 처리 성능을 극적으로 개선**한 결과를 확인할 수 있습니다.

## 🚀 성능 개선 결과

| 지표 | 기존 시스템 | Kafka 시스템 | 개선율 |
|------|-------------|--------------|--------|
| **처리량** | 44.71 req/sec | 2,739.61 req/sec | **61배 증가** |
| **응답 시간** | 223.654ms | 15.234ms | **93% 단축** |
| **실패율** | 98.2% | 0% | **100% 해결** |
| **안정성** | 타임아웃 발생 | 모든 요청 완료 | **완전 개선** |

## 📋 주요 기능

- **기존 시스템**: 동기식 주문 처리 (`/order-direct`)
- **개선된 시스템**: Kafka 기반 비동기 주문 처리 (`/order-kafka`)
- **성능 모니터링**: Apache Bench를 통한 부하 테스트
- **상세 보고서**: 성능 개선 과정 및 결과 분석

---

##  프로젝트 받기

```bash
git clone  git@github.com:hoonZeee/flashsale.git
```

---

## 자바 환경 확인

이 프로젝트는 Java 17이 필요합니다.

### 버전 확인

```bash
java -version
```

### 만약 없다면

* [Java 17 다운로드 (Adoptium)](https://adoptium.net/temurin/releases/?version=17)
* 설치 후 환경변수 설정 완료 확인

---

## 빌드 (Gradle)

Gradle이 포함되어 있으므로 별도 설치 없이 바로 실행 가능합니다.

```bash
./gradlew clean build
```

빌드가 완료되면 `build/libs` 폴더에 jar 파일이 생성됩니다.

---

## 서버 실행

```bash
./gradlew bootRun
```

또는 jar 직접 실행:

```bash
java -jar build/libs/flashsale-0.0.1-SNAPSHOT.jar
```

정상적으로 실행되면 콘솔에 다음 메시지가 표시됩니다:

```
초기 데이터 세팅 완료 (재고 1000개)
```

---

## 테스트 데이터 준비

테스트용으로 `order.json` 파일을 하나 만들어주세요.

### order.json

```json
{ "userId": 1, "productId": 1, "quantity": 1 }
```

위 내용을 복붙해서 저장하면 됩니다. (폴더 위치는 아무 데나 상관없습니다.)

---

## Apache Bench 설치

HTTP 부하 테스트 도구입니다.

### macOS

```bash
brew install httpd
```

###  Ubuntu / Linux

```bash
sudo apt-get update
sudo apt-get install apache2-utils
```

###  Windows (WSL 추천)

```bash
sudo apt-get install apache2-utils
```

설치 확인:

```bash
ab -V
```

---

## Kafka 환경 설정

### Kafka 설치 및 실행

```bash
# macOS
brew install kafka zookeeper
brew services start zookeeper
brew services start kafka

# Kafka 토픽 생성
/opt/homebrew/opt/kafka/bin/kafka-topics --create --topic order-requests --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

### 서버 실행 (Kafka 기반)

```bash
# 8081 포트로 실행 (Kafka 시스템)
./gradlew bootRun
```

## 부하 테스트 실행

### 기존 시스템 테스트 (동기식)

```bash
# 기본 테스트 (500건 요청, 동시 10명)
ab -n 500 -c 10 -p order.json -T application/json http://localhost:8080/order-direct

# 중간 부하 테스트 (5000건 요청, 동시 200명)
ab -n 5000 -c 200 -p order.json -T application/json http://localhost:8080/order-direct

# 고부하 테스트 (10000건 요청, 동시 400명)
ab -n 10000 -c 400 -p order.json -T application/json http://localhost:8080/order-direct
```

### 개선된 시스템 테스트 (Kafka 기반)

```bash
# 기본 테스트 (500건 요청, 동시 10명)
ab -n 500 -c 10 -p order.json -T application/json http://localhost:8081/order-kafka

# 중간 부하 테스트 (5000건 요청, 동시 200명)
ab -n 5000 -c 200 -p order.json -T application/json http://localhost:8081/order-kafka

# 고부하 테스트 (10000건 요청, 동시 400명)
ab -n 10000 -c 400 -p order.json -T application/json http://localhost:8081/order-kafka
```



## 📊 성능 테스트 보고서

상세한 성능 개선 과정과 결과는 다음 문서를 참고하세요:

- [성능 테스트 보고서 (최종)](./성능_테스트_보고서_최종.md) - 완전한 성능 분석 및 개선 결과

## 🏗️ 아키텍처

### 기존 시스템 (동기식)
```
Client → Controller → Service → Database
         ↓
    즉시 응답 (느림, 실패율 높음)
```

### 개선된 시스템 (Kafka 기반)
```
Client → Controller → Kafka Producer → Kafka Topic
         ↓                    ↓
    즉시 응답 (빠름)    Consumer → Service → Database
```

## 🛠️ 기술 스택

- **Backend**: Spring Boot 3.x, Java 17
- **Message Queue**: Apache Kafka 4.1.0
- **Database**: H2 (In-Memory)
- **Build Tool**: Gradle
- **Testing**: Apache Bench (ab)

## 📁 프로젝트 구조

```
src/main/java/com/swe/flashsale/
├── config/
│   ├── DataInitializerConfig.java
│   ├── KafkaConfig.java
│   └── RestClientConfig.java
├── controller/
│   ├── OrderController.java          # 기존 동기식 API
│   └── OrderKafkaController.java     # Kafka 기반 API
├── service/
│   ├── OrderService.java
│   ├── OrderProducerService.java     # Kafka Producer
│   └── OrderConsumerService.java     # Kafka Consumer
├── dto/
│   ├── OrderRequest.java
│   └── OrderMessage.java             # Kafka 메시지 DTO
└── domain/
    ├── OrderEntity.java
    ├── OrderEvent.java
    └── Product.java
```

## 🎯 실습 미션

1. **기존 시스템 테스트**: `/order-direct` API의 성능 수치를 기록합니다.
2. **Kafka 시스템 테스트**: `/order-kafka` API의 성능 수치를 기록합니다.
3. **성능 비교**: 개선 전/후 결과를 비교 분석합니다.
4. **보고서 작성**: 성능 개선 과정과 결과를 문서화합니다.
5. **브랜치 푸시**: 본인 브랜치에 결과를 push 해주세요.


