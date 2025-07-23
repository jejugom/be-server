package org.scoula.codef.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.scoula.asset.dto.AssetDetailDto;
import org.scoula.asset.service.AssetDetailService;
import org.scoula.codef.dto.ConnectedIdRequestDto;
import org.scoula.codef.service.CodefTokenService;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codef")
@Log4j2
public class CodefController {

	private final CodefTokenService codefTokenService;
	private final UserService userService;
	private final AssetDetailService assetDetailService;

	// ✅ 토큰 단순 조회용
	@GetMapping("/access-token")
	public ResponseEntity<String> getAccessToken() {
		String accessToken = codefTokenService.getAccessToken();
		if (accessToken != null)
			return ResponseEntity.ok(accessToken);
		else
			return ResponseEntity.status(500).body("Failed to get access token.");
	}

	// ✅ ConnectedId 생성 및 저장
	@PostMapping("/connected-id")
	public ResponseEntity<?> createConnectedId(@RequestBody ConnectedIdRequestDto requestDto) {
		log.info("📩 ConnectedId 생성 요청: {}", requestDto);

		if (requestDto.getAccountList() == null || requestDto.getAccountList().isEmpty()) {
			log.error("❌ accountList 누락");
			return ResponseEntity.badRequest().body("accountList는 필수입니다.");
		}

		Map<String, Object> result = codefTokenService.createConnectedId(requestDto);
		if (result == null) {
			log.error("❌ CODEF 응답이 null");
			return ResponseEntity.status(500).body("CODEF 응답 없음");
		}

		Map<String, Object> resultInfo = (Map<String, Object>)result.get("result");
		String code = (String)resultInfo.get("code");
		if (!"CF-00000".equals(code)) {
			log.error("❌ CODEF 실패 응답 코드: {}", code);
			return ResponseEntity.status(500).body("CODEF 오류: " + code);
		}

		String connectedId = (String)result.get("connectedId");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		if (userEmail != null && connectedId != null) {
			log.info("✅ ConnectedId 저장 - userEmail: {}, connectedId: {}", userEmail, connectedId);
			userService.updateConnectedId(userEmail, connectedId);
			return ResponseEntity.ok(result);
		} else {
			log.error("❌ userEmail 또는 connectedId null - {}, {}", userEmail, connectedId);
			return ResponseEntity.status(500).body("user 또는 connectedId 누락");
		}
	}

	// ✅ 계좌 목록 가져오고 DB 저장
	@GetMapping("/account-info")
	public ResponseEntity<?> getAccountInfo(
		@RequestParam String connectedId,
		@RequestParam String organization) {

		log.info("📥 계좌 정보 조회 요청: connectedId={}, organization={}", connectedId, organization);

		Map<String, Object> result = codefTokenService.getAccountInfo(connectedId, organization);
		if (result == null || !result.containsKey("data")) {
			log.error("❌ CODEF 계좌 응답 오류 또는 data 없음");
			return ResponseEntity.status(500).body("CODEF 계좌 응답 오류");
		}

		Map<String, Object> data = (Map<String, Object>)result.get("data");
		List<Map<String, Object>> resDepositTrust = (List<Map<String, Object>>)data.get("resDepositTrust");

		if (resDepositTrust == null || resDepositTrust.isEmpty()) {
			log.info("🔎 계좌는 조회 성공했으나 예금/신탁 내역이 없음");
			return ResponseEntity.ok(result); // 200 OK + 내용 없음
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		log.info("💾 예금 자산 저장 - 사용자: {}, 계좌 수: {}", userEmail, resDepositTrust.size());

		for (Map<String, Object> account : resDepositTrust) {
			try {
				AssetDetailDto asset = new AssetDetailDto();
				asset.setEmail(userEmail);
				asset.setAssetCategoryCode("1"); // 예금
				asset.setAssetName((String)account.get("resAccountName")); // 통장 이름
				asset.setAmount(Long.parseLong((String)account.get("resAccountBalance")));
				asset.setRegisteredAt(new Date());
				asset.setEndDate(null);
				asset.setBusinessType(null);

				assetDetailService.saveAssetDetail(asset);
			} catch (Exception e) {
				log.error("❗ 계좌 저장 실패: {}", e.getMessage(), e);
			}
		}

		return ResponseEntity.ok(result);
	}
}
