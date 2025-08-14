package org.scoula.auth.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.scoula.auth.dto.KakaoLoginRequestDto;
import org.scoula.auth.dto.KakaoLoginResponseDto;
import org.scoula.auth.dto.RefreshTokenRequestDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 카카오 OAuth 2.0 인증 컨트롤러
 * 카카오 로그인, 토큰 재발급, 로그아웃 등 인증 관련 API를 제공합니다.
 *
 * 주요 기능:
 * - 카카오 OAuth 콜백 처리 및 프론트엔드로 리다이렉트
 * - JWT 토큰 재발급
 * - 로그아웃 처리
 */
@Api(tags = "인증 API", description = "카카오 로그인, 토큰 재발급, 로그아웃 관련 API")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/auth")
public class AuthController {

	/** 카카오 인증 서비스 - 카카오 로그인 로직 처리 */
	private final KakaoAuthService kakaoAuthService;

	/** 프론트엔드 URL - application-dev.properties에서 주입 */
	@Value("${frontend.url}")
	private String frontendUrl;

	/**
	 * 카카오 OAuth 콜백 처리 메서드
	 *
	 * 카카오 인증 서버에서 사용자 로그인 후 리다이렉트되는 엔드포인트입니다.
	 * 인가 코드를 받아 카카오 API로 사용자 정보를 조회하고 JWT 토큰을 생성한 후,
	 * 프론트엔드로 302 리다이렉트합니다.
	 *
	 * 플로우:
	 * 1. 인가 코드로 카카오 액세스 토큰 요청
	 * 2. 카카오 액세스 토큰으로 사용자 정보 조회
	 * 3. 기존 회원 확인 또는 신규 회원 생성
	 * 4. JWT 토큰 생성 및 DB 저장
	 * 5. 프론트엔드로 토큰 정보와 함께 리다이렉트
	 *
	 * @param code 카카오에서 발급한 인가 코드
	 * @return 프론트엔드로의 302 리다이렉트 응답
	 */
	@ApiOperation(value = "카카오 로그인 콜백", notes = "카카오 서버로부터 리디렉션되어 인가 코드를 받아 로그인을 처리하고 프론트엔드로 리다이렉트합니다.")
	@ApiResponses({
		@ApiResponse(code = 302, message = "프론트엔드로 리다이렉트"),
		@ApiResponse(code = 500, message = "서버 내부 오류")
	})
	@GetMapping("/kakao/callback")
	public ResponseEntity<Void> kakaoCallback(
		@ApiParam(value = "카카오 서버에서 발급해준 인가 코드", required = true)
		@RequestParam("code") String code) {

		try {
			// 1. 카카오 로그인 처리 -> 서비스 전용 Access/Refresh 토큰 생성
			KakaoLoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);

			// 2. 신규 회원 여부 및 성향 미정의 여부 가져오기
			boolean isNew = kakaoAuthService.getLastProcessedUserIsNew();
			boolean isTendencyNotDefined = kakaoAuthService.getLastProcessedUserTendencyNotDefined();

			// 3. Access Token을 위한 HttpOnly 쿠키 생성 🍪
			// Access Token은 수명이 짧으므로 Max-Age를 짧게 설정합니다 (예: 30분)
			long accessTokenValidityInSeconds = 30 * 60;
			String accessTokenCookie = String.format(
				"accessToken=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=Lax",
				loginResponse.getAccessToken(),
				accessTokenValidityInSeconds
			);

			// 4. Refresh Token을 위한 HttpOnly 쿠키 생성 🍪
			// Refresh Token은 수명이 길게 설정됩니다 (예: 14일)
			long refreshTokenValidityInSeconds = 14 * 24 * 60 * 60;
			String refreshTokenCookie = String.format(
				"refreshToken=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=Lax",
				loginResponse.getRefreshToken(),
				refreshTokenValidityInSeconds
			);

			// 5. 프론트엔드로 리다이렉트할 URL 생성 (토큰 정보는 모두 제거)
			String redirectUrl = String.format("%s/auth/success?isNew=%s&isTendencyNotDefined=%s",
				frontendUrl,
				isNew,
				isTendencyNotDefined);

			// 6. HTTP 302 응답 생성: Location 헤더와 2개의 Set-Cookie 헤더 추가
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", redirectUrl);
			headers.add("Set-Cookie", accessTokenCookie);
			headers.add("Set-Cookie", refreshTokenCookie); // 동일한 이름의 헤더를 여러 개 추가

			return new ResponseEntity<>(headers, HttpStatus.FOUND);

		} catch (Exception e) {
			log.error("카카오 로그인 처리 중 오류 발생", e);

			// 7. 오류 발생 시 에러 페이지로 리다이렉트
			try {
				String errorRedirectUrl =
					frontendUrl + "/auth/error?message=" + URLEncoder.encode("로그인 처리 중 오류가 발생했습니다.", "UTF-8");
				return ResponseEntity.status(HttpStatus.FOUND)
					.header("Location", errorRedirectUrl)
					.build();
			} catch (UnsupportedEncodingException ue) {
				return ResponseEntity.status(HttpStatus.FOUND)
					.header("Location", frontendUrl + "/auth/error")
					.build();
			}
		}
	}

	/**
	 * 카카오 로그인 API (프론트사이드의 POST 방식)
	 *
	 * 클라이언트에서 직접 인가 코드를 전송하여 로그인을 처리하는 엔드포인트입니다.
	 * 현재 서버사이드 구현방식에서는 사용하지 않는 코드입니다.
	 * 서버사이드 구현 : 인가코드를 백엔드가 직접 받아서 처리하는 방식 (/kakao/callback 로 인가 코드가 옴)
	 *
	 * @param request 카카오 인가 코드를 포함한 요청
	 * @return 로그인 성공 시 JWT 토큰 정보, 실패 시 오류 메시지
	 */
	@ApiOperation(value = "카카오 로그인", notes = "클라이언트에서 받은 카카오 인가 코드로 로그인 및 회원가입을 처리하고 JWT 토큰을 발급합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그인 성공", response = KakaoLoginResponseDto.class),
		@ApiResponse(code = 401, message = "유효하지 않은 인가코드"),
		@ApiResponse(code = 500, message = "카카오 서버 통신 오류")
	})
	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequestDto request) {
		String code = request.getCode();
		try {
			KakaoLoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
			return ResponseEntity.ok(loginResponse);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST && e.getResponseBodyAsString().contains("KOE320")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "유효하지 않은 인가코드입니다."));
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "카카오 서버와 연결되지 않는 상태입니다. "));
		}
	}

	/**
	 * JWT 토큰 재발급 API
	 *
	 * 만료된 Access Token을 Refresh Token을 사용하여 재발급합니다.
	 * API 클라이언트의 axios interceptor 에서 401 오류 발생 시 자동으로 호출됩니다.
	 *
	 * @param requestDto Refresh Token을 포함한 요청 DTO
	 * @return 새로운 Access Token과 Refresh Token
	 */
	@ApiOperation(value = "토큰 재발급", notes = "만료된 Access Token을 Refresh Token을 사용하여 재발급합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "토큰 재발급 성공", response = TokenRefreshResponseDto.class),
		@ApiResponse(code = 401, message = "유효하지 않은 Refresh Token")
	})
	@PostMapping("/refresh")
	public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
		@ApiParam(value = "Refresh Token을 포함하는 DTO", required = true)
		@RequestBody RefreshTokenRequestDto requestDto) {
		// Refresh Token으로 새로운 토큰 쌍 발급
		TokenRefreshResponseDto responseDto = kakaoAuthService.reissueTokens(requestDto.getRefreshToken());
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 로그아웃 API
	 *
	 * 서버에 저장된 사용자의 Refresh Token을 삭제하여 로그아웃을 처리합니다.
	 * 클라이언트는 별도로 세션저장소의 토큰을 제거하고 있습니다.
	 *
	 * @param authentication Spring Security에서 제공하는 인증 정보
	 * @return HTTP 204 No Content
	 */
	@ApiOperation(value = "로그아웃", notes = "서버에 저장된 사용자의 Refresh Token을 삭제하여 로그아웃 처리합니다.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "로그아웃 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(Authentication authentication) {
		// 1. DB에서 Refresh Token 삭제 (기존 로직)
		String email = authentication.getName();
		kakaoAuthService.logout(email);

		// 2. 브라우저의 쿠키를 삭제하라는 응답 헤더 생성
		HttpHeaders headers = new HttpHeaders();
		// accessToken 쿠키를 즉시 만료시킴
		headers.add("Set-Cookie", "accessToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=Lax");
		// refreshToken 쿠키를 즉시 만료시킴
		headers.add("Set-Cookie", "refreshToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=Lax");

		// 3. 본문 없이 헤더만 담아 204 No Content 응답 전송
		return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "사용자 프로필 조회", notes = "현재 로그인된 사용자의 이메일과 이름을 반환합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "프로필 조회 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@GetMapping("/profile")
	public ResponseEntity<Map<String, String>> getProfile(Authentication authentication) {
		// 1. SecurityContext에서 인증된 사용자의 이메일 추출
		String email = authentication.getName();

		log.info("====================== 프로필 ===========================");
		log.info("email = " + email);
		log.info("====================== 프로필 ===========================");
		// 2. 이메일을 기반으로 사용자 정보 조회
		//    (실제로는 DB에서 사용자 이름을 조회해야 합니다. KakaoAuthService에 해당 로직이 있다고 가정)
		String displayName = kakaoAuthService.getDisplayNameByEmail(email);

		// 3. 프론트엔드가 필요한 JSON 형식으로 응답 생성
		Map<String, String> profile = new HashMap<>();
		profile.put("email", email);
		profile.put("displayName", displayName);

		return ResponseEntity.ok(profile);
	}
}



