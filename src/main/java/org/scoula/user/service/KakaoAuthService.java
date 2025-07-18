package org.scoula.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.user.domain.UserVO;
import org.scoula.user.dto.KakaoTokenResponseDTO;
import org.scoula.user.dto.KakaoUserInfoDTO;
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

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoAuthService {

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    private final UserMapper userMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public UserVO processKakaoLogin(String code) {
        // 1. 인가 코드로 액세스 토큰 요청
        KakaoTokenResponseDTO tokenResponse = getKakaoAccessToken(code);

        // 2. 액세스 토큰으로 사용자 정보 요청
        KakaoUserInfoDTO userInfo = getKakaoUserInfo(tokenResponse.getAccessToken());

        // 3. 사용자 정보로 회원 가입 또는 로그인 처리
        return findOrCreateUser(userInfo);
    }

    private KakaoTokenResponseDTO getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponseDTO> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                KakaoTokenResponseDTO.class
        );
        return response.getBody();
    }

    private KakaoUserInfoDTO getKakaoUserInfo(String accessToken) {
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
            return objectMapper.readValue(response.getBody(), KakaoUserInfoDTO.class);
        } catch (Exception e) {
            log.error("Failed to parse Kakao user info: ", e);
            throw new RuntimeException("Failed to parse Kakao user info", e);
        }
    }

    private UserVO findOrCreateUser(KakaoUserInfoDTO userInfo) {
        String email = userInfo.getKakaoAccount().getEmail();
        Optional<UserVO> existingUser = Optional.ofNullable(userMapper.findByEmail(email));

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // 새로운 사용자 등록
            UserVO newUser = UserVO.builder()
                    .email(email)
                    .name(userInfo.getProperties().getNickname())
                    .birth(null) // 카카오에서 생년월일 정보가 없을 수 있음
                    .gender(null) // 카카오에서 성별 정보가 없을 수 있음
                    .phone(null) // 카카오에서 전화번호 정보가 없을 수 있음
                    .password("kakao_login") // 카카오 로그인은 비밀번호가 필요 없으므로 임의 값 설정
                    .build();
            userMapper.insertUser(newUser);

            return newUser;
        }
    }
}
