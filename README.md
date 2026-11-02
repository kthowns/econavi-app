# EcoNavi

**환경을 보호하는 에코 로컬 관광 지도 앱의 백엔드입니다.**

충남톤에서 **끓는 점 2도(2팀)** 이 개발한 Spring Boot + Flutter 프로젝트입니다. 친환경 매장과 행사 장소를 탐색하고, 관심 장소를 찜하며 에코 포인트와 지급 내역을 확인할 수 있습니다.

이 저장소는 [2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE)의 fork입니다. Spring Boot API 서버와 Flutter 웹 빌드 결과물을 포함하며, Flutter 앱 소스는 이 저장소에 포함되어 있지 않습니다.

## 주요 기능

| 영역 | 구현 내용 |
| --- | --- |
| 인증·회원 | 이메일 기반 회원가입·로그인, JWT 인증, Redis 블랙리스트를 이용한 로그아웃, 회원 정보·비밀번호·닉네임 관리 |
| 장소 | 친환경 매장(STORE)·행사(EVENT) 등록·조회·수정·삭제, ID 커서 기반 목록 조회 |
| 장소 검색 | 이름 키워드 검색, 위도·경도·거리(km)를 이용한 주변 장소 조회 |
| 찜 | 사용자별 찜 목록 조회·추가·삭제 |
| 에코 포인트 | 보유 포인트와 포인트 지급 내역 조회 |
| 사진·리소스 | 프로필·장소·상품 사진 다중 업로드, 사진 메타데이터 조회, 파일 다운로드 |
| 접근 제어 | USER·STAFF 역할과 리소스 소유권에 따른 접근 검사 |

포인트 기능은 현재 조회 API 범위입니다. 상품 사진 API와 엔티티는 포함되어 있으나 상품 구매·교환 API와 리뷰 API는 현재 코드에 포함되어 있지 않습니다.

## 주요 기여

저는 백엔드 초기 구성과 배포 자동화부터 인증, 장소·찜·회원·포인트·사진 API 및 테스트를 구현했습니다. 아래 내용은 원본 저장소에 병합된 제 PR을 기준으로 정리했습니다.

| 영역 | 제가 수행한 작업 | 관련 PR |
| --- | --- | --- |
| 초기 구성·CI/CD | Spring Boot, Swagger, CORS, 프로필 설정과 GitHub Actions 파이프라인을 구성하고 Docker·EC2 배포 및 HTTPS 적용을 진행했습니다. | [#2](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/2), [#6](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/6) |
| 인증·예외 처리 | Spring Security + JWT 인증, 회원가입·로그인·로그아웃, Redis 토큰 블랙리스트와 공통 예외 처리를 구현했습니다. | [#8](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/8), [#12](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/12) |
| 장소·검색 | 장소 CRUD와 단위·통합 테스트, 키워드·주변 장소 검색을 구현하고 조회 파라미터를 정리했습니다. | [#11](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/11), [#25](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/25), [#29](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/29) |
| 찜·회원 | 찜 목록·추가·삭제와 회원 정보·비밀번호·닉네임 관리 API 및 테스트를 구현했습니다. | [#15](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/15), [#17](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/17) |
| 포인트 | 보유 포인트·지급 히스토리 조회 API와 통합 테스트를 작성했습니다. | [#21](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/21) |
| 사진·파일 | 파일 저장·다운로드를 구현하고 사진 로직을 별도 도메인으로 분리했습니다. 프로필·장소·상품 사진 업로드와 메타데이터 조회 API 및 통합 테스트를 작성했습니다. | [#27](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/27), [#31](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/31), [#33](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/33) |
| 검증·문서 개선 | 필드별 Validation 오류 응답과 인증 입력 검증을 수정하고 Swagger의 인증 헤더 문제를 해결했습니다. | [#13](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/13), [#23](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pull/23) |

전체 내역은 [원본 저장소 PR 목록](https://github.com/2025Chungnamthon/2025_CHUNGNAM_TEAM_2_BE/pulls?q=is%3Apr)에서 확인할 수 있습니다.

## 기술 스택

| 구분 | 기술 |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5.3, Spring Security, Spring Data JPA, Bean Validation, JWT |
| Database·Cache | MySQL, Redis |
| API 문서·테스트 | springdoc-openapi / Swagger UI, JUnit 5, Spring Boot Test, MockMvc |
| 배포 | GitHub Actions, Docker, Docker Compose, AWS EC2, Nginx, Certbot |
| 클라이언트 | Flutter — 이 저장소에는 웹 빌드 결과물 포함 |

## 프로젝트 구조

```text
src/
├── main/
│   ├── java/com/example/econavi/
│   │   ├── auth/       # 인증·JWT·Security
│   │   ├── member/     # 회원
│   │   ├── place/      # 장소·검색·찜
│   │   ├── product/    # 포인트·지급 내역·상품 엔티티
│   │   ├── photo/      # 사진 업로드·메타데이터
│   │   └── common/     # 파일 저장·접근 검사·예외 처리
│   └── resources/
│       ├── application.yml
│       ├── schema.sql
│       └── static/     # Flutter 웹 빌드 결과물
└── test/               # 단위·통합 테스트
.github/workflows/      # 테스트·배포 워크플로
docs/api.md             # REST API 명세
```

## 로컬 실행

JDK 21, MySQL, Redis가 필요합니다. Gradle Wrapper는 저장소에 포함되어 있습니다.

1. MySQL에 데이터베이스와 접근 계정을 준비합니다.
2. 아래 환경변수를 설정합니다. 값은 로컬 개발용 예시입니다.
3. 저장소 루트에서 서버를 실행합니다.

```powershell
$env:DB_HOST = 'localhost'
$env:DB_PORT = '3306'
$env:DB_NAME = 'econavi_local'
$env:DB_USER = 'econavi_local'
$env:DB_PASSWORD = 'your-local-db-password'
$env:REDIS_HOST = 'localhost'
$env:REDIS_PORT = '6379'
$env:JWT_SECRET = 'local-development-secret-key-at-least-32-bytes-long'
$env:JWT_ACCESS_EXP = '3600000'
.\gradlew.bat bootRun --args='--spring.profiles.active=local --spring.jpa.hibernate.ddl-auto=update'
```

위 명령은 저장소의 공통 설정에 환경변수를 주입해 실행합니다. `local` 프로필을 명시해 기본 `dev` 프로필과 로컬에 남아 있을 수 있는 개발 설정의 영향을 피하고, 개발 DB 테이블을 생성·갱신합니다. macOS·Linux에서는 환경변수를 `export`로 설정하고 `./gradlew`를 사용합니다.

[application.yml](src/main/resources/application.yml)은 기본 프로필을 `dev`로 지정하지만, `application-*.yml`은 Git 추적 대상에서 제외됩니다. 새로 clone한 환경에는 개발·테스트 프로필 설정이 없으므로 필요한 설정을 직접 준비해야 합니다. Spring Boot는 프로젝트 루트의 `.env`를 자동으로 읽지 않으므로 위와 같이 프로세스 환경변수로 전달합니다.

| 항목 | 로컬 주소 |
| --- | --- |
| 서버 | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/my-docs` |
| OpenAPI JSON | `http://localhost:8080/api-spec` |

사진 파일은 [FileStorageService](src/main/java/com/example/econavi/common/service/FileStorageService.java)의 `/opt/econavi/uploads`에 저장합니다. 사진 기능을 실행하려면 해당 경로에 쓰기 권한을 확보하거나 실행 환경에 맞게 저장 경로를 조정해야 합니다. 업로드 제한은 파일당 10MB, 요청당 50MB입니다.

## API 문서

기존 API 요약과 장소 등록 요청 예시는 [REST API 명세서](docs/api.md)에 정리했습니다. 상세 요청·응답 및 쿼리 파라미터는 서버 실행 후 Swagger UI에서 확인할 수 있습니다.

인증이 필요한 요청에는 `Authorization: Bearer <JWT_TOKEN>` 헤더를 사용합니다. API 경로는 `/auth`, `/member`, `/place`, `/bookmark`, `/point`, `/photo`, `/download`로 시작하며 공통 `/api` 접두사는 없습니다.

## 테스트

회원·장소·찜 단위 테스트와 인증·장소·찜·포인트·사진 통합 테스트가 포함되어 있습니다. 통합 테스트는 MySQL·Redis 및 `test` 프로필 설정을 필요로 합니다. 테스트 전용 DB와 접속 설정을 준비한 뒤 실행합니다.

```powershell
.\gradlew.bat test '-Dspring.profiles.active=test'
```

`test` 프로필에는 DB·Redis·JWT 설정과 테스트 테이블 생성 설정을 준비해야 합니다. 공통 설정을 사용할 경우 앞의 환경변수를 지정하고 `SPRING_JPA_HIBERNATE_DDL_AUTO=update` 등으로 테스트 DB 스키마를 준비할 수 있습니다. [schema.sql](src/main/resources/schema.sql)은 스키마 참고 자료이며 공통 설정에는 자동 실행이 지정되어 있지 않습니다.

## CI/CD 및 배포

[배포 워크플로](.github/workflows/deploy-main.yml)는 `main` push 시 Docker 이미지를 빌드·푸시하고, Compose 파일을 서버로 복사한 뒤 SSH로 배포합니다. Dockerfile은 Java 21 환경에서 JAR를 빌드하며 이미지 빌드 중 테스트는 제외합니다.

fork에서 사용하려면 `econavi-backend` GitHub Environment와 DB·Redis·JWT·Docker Hub·서버 접속 관련 Secrets가 필요합니다. [docker-compose.yml](docker-compose.yml)은 외부 네트워크 `econavi-net`을 전제로 하고 호스트 포트를 직접 공개하지 않으므로 서버의 네트워크·리버스 프록시 구성을 함께 준비해야 합니다.

PR 대상 테스트 워크플로도 포함되어 있으나, 현재 `test-main.yml`·`test-dev.yml`은 작업 경로를 실제 루트와 다른 `./econavi`로 지정하고 Java 17을 설정합니다. 현재 프로젝트는 Java 21이므로 그대로 재사용하려면 경로·JDK·테스트 환경 설정을 먼저 맞춰야 합니다.

기존 배포 주소는 `https://econavi.kthowns.cloud`이며 현재 운영 여부는 확인하지 않았습니다.
