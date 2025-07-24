package org.scoula.auth.controller;

import java.util.Map;


import org.scoula.auth.dto.LoginResponseDto;
import org.scoula.auth.dto.RefreshTokenRequestDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
// @RequestMapping	// redirect 변경 시 수정
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	// ✅ 이 핸들러가 카카오 리디렉션을 처리함
	@GetMapping("/kakao/callback")
	public ResponseEntity<LoginResponseDto> kakaoCallback(@RequestParam("code") String code) {
		LoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
		return ResponseEntity.ok(loginResponse);
	}

	// ✅ 반환 타입을 ResponseEntity<LoginResponseDto>로 수정
	@PostMapping("/kakao")
	public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody Map<String, String> body) {
		String code = body.get("code");

		// 서비스는 LoginResponseDto를 반환
		LoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);

		// 수정된 반환 타입과 일치하므로 에러가 발생하지 않음
		return ResponseEntity.ok(loginResponse);
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
}
