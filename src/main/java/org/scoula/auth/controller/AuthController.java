package org.scoula.auth.controller;

import java.util.Map;

import org.scoula.auth.dto.KakaoLoginRequestDto;
import org.scoula.auth.dto.KakaoLoginResponseDto;
import org.scoula.auth.dto.RefreshTokenRequestDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.service.KakaoAuthService;
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

@Api(tags = "인증 API", description = "카카오 로그인, 토큰 재발급, 로그아웃 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	@ApiOperation(value = "카카오 로그인 콜백", notes = "카카오 서버로부터 리디렉션되어 인가 코드를 받아 로그인을 처리합니다. (주로 서버 간 통신용)")
	@ApiResponses({
		@ApiResponse(code = 200, message = "로그인 성공", response = KakaoLoginResponseDto.class),
		@ApiResponse(code = 500, message = "서버 내부 오류")
	})
	@GetMapping("/kakao/callback")
	public ResponseEntity<KakaoLoginResponseDto> kakaoCallback(
		@ApiParam(value = "카카오 서버에서 발급해준 인가 코드 \nex) { \"code\" : \"s2sij32sji..\"}", required = true)
		@RequestParam("code") String code) {
		KakaoLoginResponseDto loginResponse = kakaoAuthService.processKakaoLogin(code);
		return ResponseEntity.ok(loginResponse);
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
		@ApiResponse(code = 200, message = "토큰 재발급 성공", response = TokenRefreshResponseDto.class),
		@ApiResponse(code = 401, message = "유효하지 않은 Refresh Token")
	})
	@PostMapping("/refresh")
	public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
		@ApiParam(value = "Refresh Token을 포함하는 DTO", required = true)
		@RequestBody RefreshTokenRequestDto requestDto) {
		TokenRefreshResponseDto responseDto = kakaoAuthService.reissueTokens(requestDto.getRefreshToken());
		return ResponseEntity.ok(responseDto);
	}

	@ApiOperation(value = "로그아웃", notes = "서버에 저장된 사용자의 Refresh Token을 삭제하여 로그아웃 처리합니다.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "로그아웃 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(Authentication authentication) {
		String userEmail = authentication.getName();
		kakaoAuthService.logout(userEmail);
		return ResponseEntity.noContent().build();
	}
}
