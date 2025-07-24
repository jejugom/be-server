package org.scoula.user.controller;

import java.util.List;
import java.util.Map;

import org.scoula.security.util.JwtProcessor;
import org.scoula.user.domain.UserVo;
import org.scoula.user.dto.AuthResultDto;
import org.scoula.user.dto.UserInfoDto;
import org.scoula.user.service.KakaoAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;
	private final JwtProcessor jwtProcessor;

	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
		String code = body.get("code");
		try {
			// 1. 카카오 로그인 처리 (토큰 요청 + 사용자 조회 + DB 저장)
			UserVo kakaoUser = kakaoAuthService.processKakaoLogin(code);
			// 2. JWT 발급
			String token = jwtProcessor.generateToken(kakaoUser.getEmail());
			return ResponseEntity.ok(new AuthResultDto(token, UserInfoDto.of(kakaoUser)));

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST &&
				e.getResponseBodyAsString().contains("KOE320")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error","유효하지 않은 인가코드입니다."));

			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error","카카오 서버와 연결되지 않는 상태입니다. "));

		}

	}
}

