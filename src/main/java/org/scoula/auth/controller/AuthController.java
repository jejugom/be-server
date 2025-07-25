package org.scoula.auth.controller;

import java.util.Map;

import org.scoula.auth.dto.LoginResponseDto;
import org.scoula.auth.dto.RefreshTokenRequestDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.service.KakaoAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
// @RequestMapping	// redirect 변경 시 수정
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final KakaoAuthService kakaoAuthService;

	// ✅ 이 핸들러가 카카오 리디렉션을 처리함
	@GetMapping("/kakao/callback")
	public ResponseEntity<LoginResponseDto> kakaoCallback(@RequestParam("code") String code) {
		LoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
		return ResponseEntity.ok(loginResponse);
	}

	/***
	 * 개발 단계에서는 callBack호출 시 바로 화면에 LoginDTO 띄워주게끔
	 * 이후 프론트 연동 시 /kakao/callback 매핑 주석/삭제 처리
	 * 현 단계에서는 /auth/kakao 로 카카오 인가코드 삽입 시 에러 나는것이 정상.
	 */

	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
		String code = body.get("code");
		try {
			// 1. 카카오 로그인 처리 (토큰 요청 + 사용자 조회 + DB 저장)
			// 서비스는 LoginResponseDto를 반환
			LoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
			// 2. JWT 발급
			return ResponseEntity.ok(loginResponse);

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST &&
				e.getResponseBodyAsString().contains("KOE320")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "유효하지 않은 인가코드입니다."));

			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "카카오 서버와 연결되지 않는 상태입니다. "));

		}

	}

	/**
	 * Access Token과 Refresh Token을 재발급합니다.
	 * @return 새로운 Access Token
	 */
	@PostMapping("/refresh")
	public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
		@RequestBody RefreshTokenRequestDto requestDto) {

		TokenRefreshResponseDto responseDto = kakaoAuthService.reissueTokens(requestDto.getRefreshToken());
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(Authentication authentication) {
		// SecurityContext에서 현재 인증된 사용자의 이메일을 가져온다.
		String userEmail = authentication.getName();
		kakaoAuthService.logout(userEmail);

		return ResponseEntity.noContent().build();
	}
}
