package org.scoula.codef.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.scoula.codef.dto.ConnectedIdRequestDto;
import org.scoula.codef.util.CodefApiClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CodefTokenService {

	private final CodefApiClient codefApiClient;

	// TODO: Access Token을 안전하게 저장하고 관리하는 로직 추가 필요
	private String accessToken;
	private long tokenExpiryTime; // 토큰 만료 시간 (밀리초)

	@PostConstruct
	public void init() {
		codefApiClient.setCodefTokenService(this);
	}

	public String getAccessToken() {
		// TODO: 토큰 만료 여부 확인 및 갱신 로직 추가
		if (accessToken == null || isTokenExpired()) {
			log.info("Access Token is null or expired. Publishing new token...");
			Map<String, Object> tokenMap = codefApiClient.publishToken();
			if (tokenMap != null && tokenMap.containsKey("access_token")) {
				this.accessToken = (String)tokenMap.get("access_token");
				// 토큰 유효 기간 (초)
				int expiresIn = (Integer)tokenMap.get("expires_in");
				this.tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L); // 현재 시간 + 유효 기간
				log.info("New Access Token published successfully.");
			} else {
				log.error("Failed to publish new Access Token.");
				return null;
			}
		}
		return accessToken;
	}

	private boolean isTokenExpired() {
		// 만료 시간보다 현재 시간이 크면 토큰 만료
		return System.currentTimeMillis() >= tokenExpiryTime;
	}

	public Map<String, Object> createConnectedId(ConnectedIdRequestDto requestDto) {
		try {
			// 리스트 내부의 account 객체 수정 (id/password 암호화)
			for (Map<String, Object> account : requestDto.getAccountList()) {
				if (account.containsKey("password")) {
					// String encryptedPassword = codefApiClient.encryptRSA((String)account.get("password"),
					// 	codefApiClient.getPublicKey());
					String encryptedPassword = codefApiClient.encryptRSA((String)account.get("password"),
                            codefApiClient.getPublicKey())
                        .replaceAll("\n", "");
                    account.put("password", encryptedPassword);
                }
                log.info("🔐 암호화 후 account: {}", account); // ✅ 추가

            }

			// 계정 리스트 통째로 담은 요청
			Map<String, Object> bodyMap = new HashMap<>();
			bodyMap.put("accountList", requestDto.getAccountList());

			return codefApiClient.createConnectedId(bodyMap);

		} catch (Exception e) {
			log.error("Error while creating ConnectedId: {}", e.getMessage(), e);
			return null;
		}
	}

	public Map<String, Object> getAccountInfo(String connectedId, String organization) {
		return codefApiClient.getAccountInfo(connectedId, organization);
	}
}
