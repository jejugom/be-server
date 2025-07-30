package org.scoula.View.codef.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;


import org.scoula.asset.dto.AssetStatusRequestDto;

import org.scoula.asset.service.AssetStatusService;
import org.scoula.View.codef.dto.ConnectedIdRequestDto;
import org.scoula.View.codef.util.CodefApiClient;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
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
	private final AssetStatusService assetStatusService;
	private final UserService userService;


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
				log.info(accessToken);
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

	public void saveAccountInfo(String userEmail, String connectedId) {
		String organization = "0004";
		log.info("📥 계좌 정보 조회 요청: connectedId={}, organization={}", connectedId, organization);

		Map<String, Object> result = getAccountInfo(connectedId, organization);
		if (result == null || !result.containsKey("data")) {
			log.error("❌ CODEF 계좌 응답 오류 또는 data 없음");
			throw new RuntimeException("CODEF에서 계좌 정보를 받아올 수 없습니다.");
		}

		Map<String, Object> data = (Map<String, Object>)result.get("data");
		List<Map<String, Object>> resDepositTrust = (List<Map<String, Object>>)data.get("resDepositTrust");

		if (resDepositTrust == null || resDepositTrust.isEmpty()) {
			log.info("🔎 계좌는 조회 성공했으나 예금/신탁 내역이 없음");
			return; // 200 OK + 내용 없음
		}

		log.info("💾 예금 자산 저장 - 사용자: {}, 계좌 수: {}", userEmail, resDepositTrust.size());

		for (Map<String, Object> account : resDepositTrust) {
			try {
				AssetStatusRequestDto asset = new AssetStatusRequestDto();
				asset.setAssetCategoryCode("2"); // 예적금
				asset.setAssetName((String)account.get("resAccountName")); // 통장 이름
				asset.setAmount(Long.parseLong((String)account.get("resAccountBalance")));
				asset.setBusinessType(null);
				assetStatusService.addAssetStatus(userEmail,asset);
				/**
				 * 사용자 총 자산에 Codef에서 불러온 계좌 자산 금액 추가
				 */
				UserDto userDto = userService.getUser(userEmail);
				Long curBalance = userDto.getAsset();
				curBalance += Long.parseLong((String)account.get("resAccountBalance"));
				userDto.setAsset(curBalance);
				userService.updateUser(userEmail,userDto);



			} catch (Exception e) {
				log.error("❗ 계좌 저장 실패: {}", e.getMessage(), e);
				throw new RuntimeException("계좌 저장에 실패했습니다.");
			}
		}

		return;

	}

	public Map<String, Object> getAccountInfo(String connectedId, String organization) {
		return codefApiClient.getAccountInfo(connectedId, organization);
	}
}
