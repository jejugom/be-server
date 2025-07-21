# KB 최종 프로젝트 Backend

이 프로젝트는 KB 최종 프로젝트의 백엔드 시스템입니다. Spring Framework를 기반으로 구축되었으며, 새로운 ERD에 맞춰 도메인 구조를 재편하고 카카오 로그인 기능을 통합했습니다.

## 주요 기능

*   **ERD 기반 도메인 모델:** `user`, `category`, `asset_detail`, `asset_info`, `custom_recommend`, `faq`, `branch`, `booking` 등 새로운 ERD에 정의된 데이터 모델을 기반으로 구현되었습니다.
*   **RESTful API:** 각 도메인에 대한 CRUD(Create, Read, Update, Delete) 작업을 위한 RESTful API 엔드포인트가 제공됩니다.
*   **카카오 로그인 통합:** 외부 인증 시스템으로 카카오 로그인을 지원합니다. 사용자는 카카오 계정을 통해 간편하게 로그인할 수 있습니다.
*   **JWT 기반 인증:** 로그인 성공 시 JWT(JSON Web Token)를 발급하여 API 요청에 대한 인증을 처리합니다.
*   **Spring Security:** 보안 설정은 Spring Security를 통해 관리되며, 모든 API는 현재 `permitAll()`로 설정되어 있어 인증 없이 접근 가능합니다. (개발 및 테스트 편의를 위함)

## 카카오 로그인 설정

카카오 로그인 기능을 사용하려면 `src/main/resources/application.properties` 파일에 카카오 개발자 애플리케이션 정보를 설정해야 합니다.

1.  **카카오 개발자 센터 설정:**
    *   [카카오 개발자 센터](https://developers.kakao.com/)에 로그인합니다.
    *   애플리케이션을 생성하거나 기존 애플리케이션을 선택합니다.
    *   **"내 애플리케이션" > "앱 설정" > "요약 정보"**에서 **"앱 키"** 중 **"REST API 키"**를 확인합니다. 이 값이 `kakao.client.id`에 해당합니다.
    *   **"내 애플리케이션" > "앱 설정" > "카카오 로그인" > "Redirect URI"**에 `http://localhost:8080/kakao/callback`을 추가합니다. 이 값이 `kakao.redirect.uri`에 해당합니다.

2.  **`application.properties` 설정:**
    `src/main/resources/application.properties` 파일을 열고 다음 값을 실제 카카오 개발자 센터에서 확인한 정보로 업데이트합니다.

    ```properties
    kakao.client.id=YOUR_KAKAO_REST_API_KEY
    kakao.redirect.uri=http://localhost:8080/kakao/callback
    ```
    (`YOUR_KAKAO_REST_API_KEY` 부분을 실제 REST API 키로 변경해야 합니다.)

## 프로젝트 빌드 및 실행

프로젝트를 빌드하고 실행하는 방법은 다음과 같습니다.

### 빌드

```bash
./gradlew build
```

### 실행 (Tomcat 등 웹 서버 배포)

빌드 후 생성된 `.war` 파일을 Tomcat과 같은 웹 애플리케이션 서버에 배포하여 실행할 수 있습니다.

### 개발 환경에서 실행 (IDE 사용)

IntelliJ IDEA와 같은 IDE를 사용하는 경우, Gradle 프로젝트로 임포트한 후 내장 Tomcat 또는 Spring Boot 실행 설정을 통해 애플리케이션을 실행할 수 있습니다.

## API 엔드포인트 (예시)

*   **카카오 로그인:** `POST /auth/kakao` (Request Body: `{"code": "YOUR_KAKAO_AUTH_CODE"}`)
*   **사용자 조회:** `GET /api/user/{email}`
*   **사용자 등록:** `POST /api/user/join`
*   **자산 카테고리 조회:** `GET /api/categories`
*   **지점 정보 조회:** `GET /api/branches`
*   ... (다른 도메인에 대한 API는 각 컨트롤러 파일을 참조하세요.)

## 기술 스택

*   Java 16+
*   Spring Framework (Core, Web MVC, Security)
*   MyBatis
*   Gradle
*   Lombok
*   Swagger (API 문서화)
*   MySQL (데이터베이스)
*   Kakao API
