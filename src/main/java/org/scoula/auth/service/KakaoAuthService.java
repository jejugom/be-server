package org.scoula.auth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.scoula.auth.dto.KakaoTokenResponseDto;
import org.scoula.auth.dto.KakaoUserInfoDto;
import org.scoula.auth.dto.LoginResponseDto;
import org.scoula.auth.dto.RefreshTokenDto;
import org.scoula.auth.mapper.RefreshTokenMapper;
import org.scoula.security.util.JwtProcessor;
import org.scoula.user.domain.UserVo;
import org.scoula.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
	public LoginResponseDto processKakaoLogin(String code) {
		// 1. 카카오 API를 통해 사용자 정보 받아오기
		KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(getKakaoAccessToken(code).getAccessToken());

		// 2. DB에서 사용자를 찾거나 새로 가입시키기
		UserVo user = findOrCreateUser(kakaoUserInfo);

		// 3. 우리 서비스의 Access Token과 Refresh Token 생성
		String accessToken = jwtProcessor.generateAccessToken(user.getEmail());
		String refreshTokenValue = jwtProcessor.generateRefreshToken(user.getEmail());

		// 4. 생성된 리프레시 토큰 정보를 DB에 저장 (핵심 변경 부분)
		RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
			.userEmail(user.getEmail())
			.provider("OUR_SERVICE")
			.tokenValue(refreshTokenValue)
			.expiresAt(LocalDateTime.now().plusWeeks(2)) // 2주 후 만료
			.build();

		refreshTokenMapper.saveRefreshToken(refreshTokenDto); // Mapper 메서드 호출

		// 5. 클라이언트에게 전달할 최종 응답 DTO 생성
		return LoginResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshTokenValue)
			.userId(user.getEmail())
			.userName(user.getUserName())
			.build();
	}

	private KakaoTokenResponseDto getKakaoAccessToken(String code) {
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

	// public KakaoTokenResponseDto getKakaoAccessToken(String code) {
	//     String tokenUrl = "https://kauth.kakao.com/oauth/token";
	//
	//     HttpHeaders headers = new HttpHeaders();
	//     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	//
	//     MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	//     params.add("grant_type", "authorization_code");
	//     params.add("client_id", kakaoClientId);           // ✅ application.properties에 있는 값
	//     params.add("redirect_uri", kakaoRedirectUri);     // ✅ 반드시 카카오 콘솔과 일치해야 함
	//     params.add("code", code);                         // ✅ postman에서 받은 인가코드
	//
	//     HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
	//
	//     try {
	//         // 💡 디버깅용: 먼저 문자열로 응답 확인
	//         ResponseEntity<String> response = restTemplate.exchange(
	//             tokenUrl,
	//             HttpMethod.POST,
	//             kakaoTokenRequest,
	//             String.class
	//         );
	//
	//         System.out.println("✅ 카카오 응답 본문:\n" + response.getBody());
	//
	//         // 💡 응답을 DTO로 변환
	//         ObjectMapper objectMapper = new ObjectMapper();
	//         return objectMapper.readValue(response.getBody(), KakaoTokenResponseDto.class);
	//
	//     } catch (HttpClientErrorException | HttpServerErrorException e) {
	//         System.out.println("❌ 카카오 요청 실패:");
	//         System.out.println("상태 코드: " + e.getStatusCode());
	//         System.out.println("응답 본문: " + e.getResponseBodyAsString());
	//
	//         // 💡 에러 본문을 파싱해서 사용자에게 전달해도 됨
	//         throw new RuntimeException("카카오 토큰 요청 중 오류 발생");
	//     } catch (Exception e) {
	//         e.printStackTrace();
	//         throw new RuntimeException("카카오 응답 파싱 중 오류 발생", e);
	//     }
	// }

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

		if (email == null) {
			throw new RuntimeException("카카오 사용자 이메일이 존재하지 않습니다.");
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
			.branchName(null)
			.connectedId(null)
			.build();

		userMapper.save(newUser);
		return newUser;
	}

}
