# 🌿 EcoNavi REST API Specification (v1.0.0)

친환경 장소 및 에코 포인트 관리 서비스, **EcoNavi**의 백엔드 API 명세서입니다.

---

## 🌐 서버 정보
* **EC2 배포 서버:** `https://econavi.kthowns.cloud`
* **로컬 서버:** `http://localhost:8080`

## 🔑 인증 방식
* **Bearer Auth:** `Authorization` 헤더에 `Bearer {JWT_TOKEN}` 형식으로 전송

---

## 📂 API 요약

### 1. 인증 및 사용자 서비스 (`Auth & Member`)
| Method | Path | Summary | Details |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/signup` | 회원가입 | 이메일/비밀번호/역할(USER, STAFF) |
| `POST` | `/auth/login` | 로그인 | JWT 토큰 및 회원 식별자 반환 |
| `POST` | `/auth/logout` | 로그아웃 | |
| `GET` | `/member/detail` | 사용자 정보 조회 | 식별자 기반 조회 |
| `PATCH` | `/member/pschn` | 비밀번호 변경 | 8자 이상, 특수문자 포함 필수 |
| `PATCH` | `/member/nckchn/{memberId}` | 닉네임 변경 | 2~16자 한글/영문/숫자 |
| `DELETE` | `/member/delete` | 회원 삭제 | 테스트용 |

### 2. 장소 관련 서비스 (`Place`)
| Method | Path | Summary | Details |
| :--- | :--- | :--- | :--- |
| `GET` | `/place` | 장소 목록 조회 | 페이징 지원 (last_id, size) |
| `POST` | `/place/add` | 장소 추가 | STAFF 권한 필요 |
| `GET` | `/place/search` | 장소 검색 | 키워드 기반 |
| `GET` | `/place/around` | 주변 장소 검색 | 위도/경도/거리(Km) 기반 |
| `PATCH` | `/place/update/{placeId}` | 장소 정보 수정 | STAFF 전용 |
| `DELETE` | `/place/delete/{placeId}` | 장소 삭제 | 작성자 본인(STAFF)만 가능 |

### 3. 이미지 및 리소스 서비스 (`Photo & Resource`)
| Method | Path | Summary | Details |
| :--- | :--- | :--- | :--- |
| `POST` | `/photo/place/{placeId}` | 장소 사진 업로드 | Multipart File 지원 |
| `POST` | `/photo/member/{memberId}` | 프로필 사진 업로드 | |
| `GET` | `/photo/place` | 장소 사진 메타데이터 조회 | |
| `GET` | `/download` | 파일 다운로드 | 정적 리소스 URL 기반 |

### 4. 에코 포인트 및 찜 서비스 (`Point & Bookmark`)
| Method | Path | Summary | Details |
| :--- | :--- | :--- | :--- |
| `GET` | `/point` | 내 포인트 조회 | |
| `GET` | `/point/histories` | 포인트 지급 내역 조회 | |
| `GET` | `/bookmark/all` | 찜한 장소 전체 조회 | |
| `POST` | `/bookmark/add/{placeId}` | 북마크 추가 | |
| `DELETE` | `/bookmark/delete/{bookmarkId}` | 북마크 해제 | |

---

## 📦 데이터 모델 (Request DTO)

### 📍 장소 추가 (`AddPlaceRequestDto`)
```json
{
  "name": "에코 샵",
  "placeType": "STORE", // STORE | EVENT
  "description": "친환경 제품 판매점",
  "address": "서울시 어딘가",
  "latitude": 37.1234,
  "longitude": 127.1234,
  "startDate": "2024-04-07T10:00:00",
  "endDate": "2024-12-31T20:00:00"
}
