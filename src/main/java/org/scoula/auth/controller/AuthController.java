package org.scoula.auth.controller;

import java.util.Map;

import org.scoula.auth.dto.LoginResponseDto;
import org.scoula.auth.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	// ✅ 반환 타입을 ResponseEntity<LoginResponseDto>로 수정
	@PostMapping("/kakao")
	public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody Map<String, String> body) {
		String code = body.get("code");

		// 서비스는 LoginResponseDto를 반환
		LoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);

		// 수정된 반환 타입과 일치하므로 에러가 발생하지 않음
		return ResponseEntity.ok(loginResponse);
	}
}
