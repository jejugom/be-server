package org.scoula.auth.service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.scoula.auth.dto.KakaoLoginResponseDto;
import org.scoula.auth.dto.KakaoTokenResponseDto;
import org.scoula.auth.dto.KakaoUserInfoDto;
import org.scoula.auth.dto.RefreshTokenDto;
import org.scoula.auth.dto.TokenRefreshResponseDto;
import org.scoula.auth.mapper.RefreshTokenMapper;
import org.scoula.security.util.JwtProcessor;
import org.scoula.user.domain.UserVo;
import org.scoula.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoAuthService {
	@Value("${kakao.client.id}")
	private String kakaoClientId;

	@Value("${kakao.redirect.uri}")
	private String kakaoRedirectUri;

	private final UserMapper userMapper;
	private final RefreshTokenMapper refreshTokenMapper;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final JwtProcessor jwtProcessor;

	@Transactional
	public KakaoLoginResponseDto processKakaoLogin(String code) {
		// 1. 카카오 API를 통해 사용자 정보 받아오기
		KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(getKakaoAccessToken(code).getAccessToken());

		// 2. DB에서 사용자를 찾거나 새로 가입시키기
		UserVo user = findOrCreateUser(kakaoUserInfo);

		// 3. 우리 서비스의 Access Token과 Refresh Token 생성
		String accessToken = jwtProcessor.generateAccessToken(user.getEmail());
		String refreshTokenValue = jwtProcessor.generateRefreshToken(user.getEmail());

		// 4. 생성된 리프레시 토큰 정보를 DB에 저장 (핵심 변경 부분)
		RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
			.email(user.getEmail())
			.tokenValue(refreshTokenValue)
			.expiresAt(LocalDateTime.now().plusWeeks(2)) // 2주 후 만료
			.build();

		refreshTokenMapper.saveRefreshToken(refreshTokenDto); // Mapper 메서드 호출

		// 5. 클라이언트에게 전달할 최종 응답 DTO 생성
		return KakaoLoginResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshTokenValue)
			.userId(user.getEmail())
			.userName(user.getUserName())
			.build();
	}

	private KakaoTokenResponseDto getKakaoAccessToken(String code) {
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		String tokenUrl = "https://kauth.kakao.com/oauth/token";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", kakaoRedirectUri);
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
			tokenUrl,
			HttpMethod.POST,
			kakaoTokenRequest,
			KakaoTokenResponseDto.class
		);
		return response.getBody();
	}

	private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
		String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
			userInfoUrl,
			HttpMethod.POST,
			kakaoUserInfoRequest,
			String.class
		);

		try {
			// Kakao API 응답을 KakaoUserInfoDTO로 매핑
			return objectMapper.readValue(response.getBody(), KakaoUserInfoDto.class);
		} catch (Exception e) {
			log.error("Failed to parse Kakao user info: ", e);
			throw new RuntimeException("Failed to parse Kakao user info", e);
		}
	}

	private UserVo findOrCreateUser(KakaoUserInfoDto userInfo) {
		String email = null;
		if (userInfo.getKakaoAccount() != null) {
			email = userInfo.getKakaoAccount().getEmail();
		}

		Optional<UserVo> existingUser = Optional.ofNullable(userMapper.findByEmail(email));
		if (existingUser.isPresent()) {
			return existingUser.get();
		}

		// 생년월일 파싱
		Date birthDate = null;
		if (userInfo.getKakaoAccount() != null && userInfo.getKakaoAccount().getBirthyear() != null
			&& userInfo.getKakaoAccount().getBirthday() != null) {

			String birthString = userInfo.getKakaoAccount().getBirthyear()
				+ userInfo.getKakaoAccount().getBirthday();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				birthDate = sdf.parse(birthString);
			} catch (ParseException e) {
				log.warn("생년월일 파싱 실패: " + birthString, e);
			}
		}

		// 닉네임 추출 (properties → kakao_account.profile 순서)
		String nickname = "카카오유저";
		if (userInfo.getProperties() != null && userInfo.getProperties().getNickname() != null) {
			nickname = userInfo.getProperties().getNickname();
		} else if ((userInfo.getKakaoAccount() != null) && (userInfo.getKakaoAccount().getProfile() != null) && (
			userInfo.getKakaoAccount().getProfile().getNickname() != null)) {
			nickname = userInfo.getKakaoAccount().getProfile().getNickname();
		}

		// 새로운 사용자 등록
		UserVo newUser = UserVo.builder()
			.email(email)
			.userName(nickname)
			.birth(birthDate)
			.userPhone(null)
			.branchId(null)
			.connectedId(null)
			.filename1(null)
			.filename2(null)
			.martialStatus(null)
			.incomeRange(null)
			.assetProportion(null)
			.tendency(null)
			.segment(null)
			.asset(0L)
			.build();
		userMapper.save(newUser);


		return newUser;
	}

	@Transactional
	public TokenRefreshResponseDto reissueTokens(String refreshToken) {
		// 1. 기존 Refresh Token 검증 (DB와 대조)
		String userEmail = jwtProcessor.getUsername(refreshToken);
		RefreshTokenDto storedToken = refreshTokenMapper.findTokenByUserEmail(userEmail);

		if (storedToken == null || !storedToken.getTokenValue().equals(refreshToken)) {
			throw new RuntimeException("Invalid Refresh Token");
		}

		// 2. 새로운 Access Token과 Refresh Token 생성
		String newAccessToken = jwtProcessor.generateAccessToken(userEmail);
		String newRefreshToken = jwtProcessor.generateRefreshToken(userEmail);

		// 3. DB에 새로운 Refresh Token으로 갱신
		RefreshTokenDto newRefreshTokenDto = RefreshTokenDto.builder()
			.email(userEmail)
			.tokenValue(newRefreshToken)
			.expiresAt(LocalDateTime.now().plusWeeks(2))
			.build();
		refreshTokenMapper.saveRefreshToken(newRefreshTokenDto);

		// 4. 두 개의 새로운 토큰을 모두 반환
		return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
	}

	/**
	 * 로그아웃을 처리합니다. DB에서 사용자의 Refresh Token을 삭제합니다.
	 * @param userEmail 로그아웃할 사용자의 이메일
	 */
	public void logout(String userEmail) {
		// ✅ provider 파라미터 없이 userEmail만 사용하여 토큰을 삭제합니다.
		refreshTokenMapper.deleteRefreshToken(userEmail);
	}
}
