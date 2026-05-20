# Mini Mall Project 🛒

Java Swing과 MySQL을 활용한 미니 쇼핑몰 프로젝트입니다.  
회원, 상품, 주문, 관리자 기능을 구현하여 JDBC 기반 DB 연동과 Java 객체지향 구조를 학습하는 것을 목표로 제작되었습니다.

---

# 📌 프로젝트 소개

본 프로젝트는 Java 21, JDBC, MySQL, Swing UI를 기반으로 제작한 미니 쇼핑몰 프로그램입니다.

사용자는 회원가입 및 로그인 후 상품을 조회하고 장바구니에 상품을 담아 주문할 수 있으며,  
관리자는 상품 및 주문 상태를 관리할 수 있습니다.

---

# 🛠 개발 환경

| 항목 | 내용 |
|---|---|
| Language | Java 21 |
| IDE | Spring Tools Suite (STS) |
| Database | MySQL |
| DB Tool | MySQL Workbench |
| UI | Java Swing |
| DB 연동 | JDBC |
| Build Tool | Gradle |
| Encoding | UTF-8 |

---

# 📂 프로젝트 구조

```text
mini_mall
├── docs
│   ├── ERD
│   ├── requirements
│   └── sql
│
├── src/main/java
│   ├── com.mini_mall
│   │
│   ├── dao
│   ├── dto
│   ├── service
│   ├── view
│   └── view.admin
│
└── src/main/resources
```

---

# 🧩 주요 기능

## 👤 회원(User)

- 회원가입
- 로그인
- 권한(Role) 기반 화면 분기
- 회원 정보 조회

---

## 📦 상품(Product)

- 상품 목록 조회
- 상품 상세 조회
- 재고 상태 확인
- 장바구니 담기

---

## 🛒 주문(Order)

- 여러 상품 동시 주문
- 주문 시 가격 고정 저장
- 주문 완료 시 재고 차감
- 주문 내역 조회
- 주문 상세 조회
- 주문 취소
- 총 결제 금액 조회

---

## 🔑 관리자(Admin)

- 상품 등록
- 상품 수정
- 상품 삭제
- 전체 주문 조회
- 주문 상태 변경
  - 주문완료
  - 배송중
  - 배송완료
  - 취소

---

# 🗄 ERD

<img width="390" height="411" alt="ERD" src="https://github.com/user-attachments/assets/c4eafef5-523e-4c56-b8b6-6afa597c343c" />


## 테이블 구성

### Users

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| user_id | INT | PK |
| login_id | VARCHAR(50) | 로그인 아이디 |
| password | VARCHAR(100) | 비밀번호 |
| name | VARCHAR(50) | 사용자 이름 |
| role | VARCHAR(20) | USER / ADMIN |

---

### Product

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| product_id | INT | PK |
| product_name | VARCHAR(100) | 상품명 |
| price | INT | 가격 |
| stock | INT | 재고 |
| is_active | TINYINT | 삭제 여부 |

---

### Orders

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| order_id | INT | PK |
| user_id | INT | 주문 회원 |
| order_date | TIMESTAMP | 주문일 |
| status | VARCHAR(20) | 주문 상태 |

---

### Order_Item

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| order_item_id | INT | PK |
| order_id | INT | 주문 번호 |
| product_id | INT | 상품 번호 |
| order_quantity | INT | 주문 수량 |
| order_price | INT | 주문 당시 가격 |

---

# 🔄 주문 처리 로직

주문 시 다음 과정이 하나의 흐름으로 처리됩니다.

1. Orders 테이블 생성
2. Order_Item 데이터 생성
3. 주문 당시 상품 가격 저장
4. Product 재고 차감
5. 주문 완료 처리

주문 취소 시:

- 주문 상태 → `취소`
- 상품 재고 복구

---

# 🖥 화면 구성

## 로그인 화면
- 로그인
- 회원가입

## 상품 목록 화면
- 상품 조회
- 상품 상세 조회
- 장바구니 담기
- 주문 내역 조회
- 로그아웃

## 장바구니 화면
- 상품 수량 변경
- 상품 삭제
- 총 금액 조회
- 주문하기

## 주문 내역 화면
- 주문 목록 조회
- 총 결제 금액 조회
- 주문 상세 조회

## 관리자 화면
- 상품 등록 / 수정 / 삭제
- 전체 주문 조회
- 주문 상태 변경

---

# 🔐 권한(Role)

| 권한 | 설명 |
|---|---|
| USER | 일반 사용자 |
| ADMIN | 관리자 |

로그인 시 role 값을 확인하여 화면을 분기합니다.

---

# ⚙ DB 설정

보안을 위해 DB 접속 정보는 `db.properties` 파일로 분리하였습니다.

## db.properties

```properties
db.url=jdbc:mysql://127.0.0.1:3306/minidb?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true

db.user=your_id
db.password=your_password
```

---

# 🚀 실행 방법

## 1. MySQL DB 생성

```sql
CREATE DATABASE minidb;
```

---

## 2. SQL 실행

`docs/sql` 내부 SQL 파일 실행

---

## 3. db.properties 설정

```properties
db.user=본인계정
db.password=본인비밀번호
```

---

## 4. 프로젝트 실행

```text
Main.java 실행
```

---

# 📖 요구사항 및 인터페이스 명세서

- [요구사항 명세서](docs/requirements/요구사항%20명세서.pdf)
- [인터페이스 명세서](docs/requirements/인터페이스%20명세서.pdf)

---

# 🤝 팀원

| 이름 | 역할 |
|---|---|
| 장근창 | UI / 서비스 로직 / 백엔드 / DB |
| 조성원 | UI / 서비스 로직 / 백엔드 / DB |

---

# 📚 학습 목표

- Java 객체지향 설계
- DTO / DAO / Service 구조 이해
- JDBC 기반 DB 처리
- Swing UI 이벤트 처리
- 트랜잭션 기반 주문 처리
- Git/GitHub 협업 경험

---

# 📝 회고

본 프로젝트를 통해 Java와 MySQL을 연동한 CRUD 처리 및  
실제 쇼핑몰 형태의 비즈니스 로직 구현 과정을 경험할 수 있었습니다.
