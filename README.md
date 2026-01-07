# Portfolio SNS 프로젝트
> Thread/Instagram 스타일 SNS 포트폴리오

## 🔹 프로젝트 소개
- Java + Spring 기반 포트폴리오용 SNS
- AJAX와 fragment를 활용한 SPA 느낌 구현
- 모바일 SNS UX를 반영한 하단 네비게이션
- 게시글 작성/조회, 로그인/회원가입 기능 포함

## 🔹 기술 스택
- **Backend:** Java, Spring Boot, Spring MVC
- **Frontend:** HTML, CSS, JavaScript, jQuery, AJAX
- **DB:** Oracle
- **빌드 도구:** Maven

## 🔹 주요 기능
1. **로그인 / 회원가입**
   - 이메일, 비밀번호 기반 인증
   - 로그인 후 main 화면으로 이동
   - 회원가입 후 로그인 페이지로 리다이렉트
   - 세션 체크로 미로그인 접근 차단

2. **메인 화면 (Thread 스타일)**
   - 하단 네비게이션: 홈 | 메시지 | 글쓰기 | 알림 | 내페이지
   - AJAX를 활용한 화면 전환 → SPA 느낌 구현
   - 게시글 목록(타임라인) 기본 제공

3. **글쓰기**
   - 중앙 + 버튼 클릭 시 글 작성 페이지로 이동
   - 게시글 작성 후 홈 타임라인에 바로 반영

4. **내페이지**
   - 내가 작성한 글만 확인 가능
   - 프로필 정보 표시

5. **알림**
   - 좋아요/댓글 알림 구조 (화면만, 추후 기능 확장 가능)

## 🔹 프로젝트 구조 예시
portfolio/
│
├─ src/
│   ├─ main/
│   │   ├─ java/
│   │   │   └─ com/portfolio/controller/
│   │   │       ├─ MainController.java
│   │   │       ├─ PostController.java
│   │   │       └─ UserController.java
│   │   │
│   │   ├─ resources/
│   │   │   ├─ application.properties
│   │   │   └─ static/
│   │   │       ├─ css/
│   │   │       │   └─ main.css
│   │   │       ├─ js/
│   │   │       │   └─ main.js
│   │   │       └─ images/
│   │   │           └─ (아이콘/프로필 사진)
│   │   │
│   │   └─ webapp/
│   │       └─ WEB-INF/
│   │           ├─ views/
│   │           │   ├─ main.html       ← 메인 레이아웃
│   │           │   ├─ home.html       ← AJAX로 불러올 홈 타임라인
│   │           │   ├─ message.html    ← 메시지 화면
│   │           │   ├─ alarm.html      ← 알림 화면
│   │           │   ├─ mypage.html     ← 내페이지
│   │           │   └─ post/
│   │           │       └─ write.html  ← 글쓰기 페이지
│   │           │
│   │           └─ templates/          ← Thymeleaf 사용 시
│   │
│   └─ test/
│       └─ java/
│           └─ com/portfolio/
│
├─ pom.xml
└─ README.md

## 🔹 화면 구성
### 1️⃣ 로그인 / 회원가입
- 로그인
- 회원가입
### 2️⃣ 메인 화면 (홈 / 타임라인)
### 3️⃣ 하단 네비게이션
- 홈 | 메시지 | 글쓰기 | 알림 | 내페이지
