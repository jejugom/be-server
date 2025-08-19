package org.scoula.auth.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.scoula.auth.dto.KakaoLoginRequestDto;
import org.scoula.auth.dto.KakaoLoginResponseDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
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

@Api(tags = "인증 API", description = "카카오 로그인, 토큰 재발급, 로그아웃 관련 API")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/auth")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	@Value("${frontend.url}")
	private String frontendUrl;

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
			KakaoLoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
			boolean isNew = kakaoAuthService.getLastProcessedUserIsNew();
			boolean isTendencyNotDefined = kakaoAuthService.getLastProcessedUserTendencyNotDefined();

			// ResponseCookie 빌더를 사용하여 쿠키 생성
			ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", loginResponse.getAccessToken())
				.path("/")
				.maxAge(10) // 1시간
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax")
				.build();

			ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
				.path("/")
				.maxAge(14 * 24 * 60 * 60) // 2주
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax")
				.build();

			String redirectUrl = String.format("%s/auth/success?isNew=%s&isTendencyNotDefined=%s",
				frontendUrl, isNew, isTendencyNotDefined);

			// 생성된 쿠키를 헤더에 설정
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", redirectUrl);
			headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
			headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

			return new ResponseEntity<>(headers, HttpStatus.FOUND);

		} catch (Exception e) {
			log.error("카카오 로그인 처리 중 오류 발생", e);
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

	@ApiOperation(value = "토큰 재발급", notes = "만료된 Access Token을 Refresh Token을 사용하여 재발급합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "토큰 재발급 성공"),
		@ApiResponse(code = 401, message = "유효하지 않은 Refresh Token")
	})
	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshAccessToken(
		@CookieValue(value = "refreshToken") String refreshToken) {

		TokenRefreshResponseDto responseDto = kakaoAuthService.reissueTokens(refreshToken);

		ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", responseDto.getAccessToken())
			.path("/")
			.maxAge(60 * 60) // 1시간 (초 단위)
			.httpOnly(true)
			.secure(true)
			.sameSite("Lax")
			.build();

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", responseDto.getRefreshToken())
			.path("/")
			.maxAge(14 * 24 * 60 * 60) // 2주 (초 단위)
			.httpOnly(true)
			.secure(true)
			.sameSite("Lax")
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.build();
	}

	@ApiOperation(value = "로그아웃", notes = "서버에 저장된 사용자의 Refresh Token을 삭제하여 로그아웃 처리합니다.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "로그아웃 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(Authentication authentication) {
		String email = authentication.getName();
		kakaoAuthService.logout(email);

		ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
			.path("/")
			.maxAge(0)
			.httpOnly(true)
			.secure(true)
			.sameSite("Lax")
			.build();

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
			.path("/")
			.maxAge(0)
			.httpOnly(true)
			.secure(true)
			.sameSite("Lax")
			.build();

		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.build();
	}

	@ApiOperation(value = "사용자 프로필 조회", notes = "현재 로그인된 사용자의 이메일과 이름을 반환합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "프로필 조회 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@GetMapping("/profile")
	public ResponseEntity<Map<String, String>> getProfile(Authentication authentication) {
		// ✅ 1. 인증 객체가 null인지 먼저 확인합니다.
		if (authentication == null || !authentication.isAuthenticated()) {
			// 인증 정보가 없으면, 명시적으로 401 에러를 반환합니다.
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// ✅ 2. 이제 authentication 객체가 null이 아님이 보장됩니다.
		String email = authentication.getName();
		log.info("====================== 프로필 ===========================");
		log.info("email = " + email);
		log.info("====================== 프로필 ===========================");
		String displayName = kakaoAuthService.getDisplayNameByEmail(email);
		Map<String, String> profile = new HashMap<>();
		profile.put("email", email);
		profile.put("displayName", displayName);
		return ResponseEntity.ok(profile);
	}
}
