package org.scoula.user.controller;

import java.util.Map;

import org.scoula.security.util.JwtProcessor;
import org.scoula.user.domain.UserVo;
import org.scoula.user.dto.AuthResultDto;
import org.scoula.user.dto.UserInfoDto;
import org.scoula.user.service.KakaoAuthService;
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
	private final JwtProcessor jwtProcessor;

	@PostMapping("/kakao")
	public ResponseEntity<AuthResultDto> kakaoLogin(@RequestBody Map<String, String> body) {
		String code = body.get("code");

		// 1. 카카오 로그인 처리 (토큰 요청 + 사용자 조회 + DB 저장)
		UserVo kakaoUser = kakaoAuthService.processKakaoLogin(code);

		// 2. JWT 발급
		String token = jwtProcessor.generateToken(kakaoUser.getEmail());

		// 3. 기존 구조에 맞게 응답
		return ResponseEntity.ok(new AuthResultDto(token, UserInfoDto.of(kakaoUser)));
	}
}
