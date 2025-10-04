#  설치 및 실행 가이드 (테스트용)

이 프로젝트는 한정판 주문 폭주 상황을 시뮬레이션하기 위한 스프링 부트 실습 예제입니다.
Kafka 적용 전/후의 성능 차이를 체감해보는 것이 목표입니다.

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

##  부하 테스트 실행

서버가 실행 중일 때 터미널에서 아래 명령을 실행합니다.

### 기본 테스트 (100건 요청, 동시 10명)

```bash
ab -n 500 -c 10 -p order.json -T application/json http://localhost:8080/order-direct
```

### 중간 부하 테스트 (5000건 요청, 동시 200명)

```bash
ab -n 5000 -c 200 -p order.json -T application/json http://localhost:8080/order-direct
```

### 고부하 테스트 (10000건 요청, 동시 400명)

```bash
ab -n 10000 -c 400 -p order.json -T application/json http://localhost:8080/order-direct
```



## 실습 미션

1. `/order-direct` API의 현재 성능 수치를 기록합니다.
2. 각자 성능 개선 아이디어를 적용합니다 (예: 비동기 처리, Kafka 도입 등).
3. 개선 전/후 결과를 비교하여 보고서를 작성합니다.
4. 본인 브랜치에 결과를 push 해주세요.


