package org.scoula.codef.controller;

import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Api(tags = "자산 연동 (CODEF) API", description = "CODEF API를 이용한 계정 연결 및 자산 정보 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codef")
@Log4j2
public class CodefController {

	private final CodefTokenService codefTokenService;
	private final UserService userService;
	private final AssetDetailService assetDetailService;

	@ApiOperation(value = "CODEF Access Token 조회", notes = "서버가 현재 보유하고 있는 CODEF Access Token을 조회합니다. (주로 개발 및 디버깅용)")
	@ApiResponses({
		@ApiResponse(code = 200, message = "조회 성공", response = String.class),
		@ApiResponse(code = 500, message = "토큰 조회 실패")
	})
	@GetMapping("/access-token")
	public ResponseEntity<String> getAccessToken() {
		String accessToken = codefTokenService.getAccessToken();
		if (accessToken != null)
			return ResponseEntity.ok(accessToken);
		else
			return ResponseEntity.status(500).body("Failed to get access token.");
	}

	@ApiOperation(value = "계정 연결(ConnectedId) 생성", notes = "사용자의 금융기관 정보를 CODEF에 등록하고, 반환된 connectedId를 서버에 저장합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "ConnectedId 생성 및 저장 성공", response = Map.class),
		@ApiResponse(code = 400, message = "잘못된 요청 (계좌 정보 누락 또는 은행 아이디/비밀번호 오류)"),
		@ApiResponse(code = 500, message = "서버 오류 (CODEF 응답 오류 등)")
	})
	@PostMapping("/connected-id")
	public ResponseEntity<?> createConnectedId(@RequestBody ConnectedIdRequestDto requestDto) {
		log.info("📩 ConnectedId 생성 요청: {}", requestDto);

		if (requestDto.getAccountList() == null || requestDto.getAccountList().isEmpty()) {
			log.error("❌ accountList 누락");
			throw new IllegalArgumentException("고객의 은행 정보를 입력해주세요.");
		}

		Map<String, Object> result = codefTokenService.createConnectedId(requestDto);
		if (result == null) {
			log.error("❌ CODEF 응답이 null");
			throw new RuntimeException("CODEF에서 응답을 받아오지 못했습니다.");
		}

		Map<String, Object> resultInfo = (Map<String, Object>)result.get("result");
		String code = (String)resultInfo.get("code");
		if (!"CF-00000".equals(code)) {
			log.error("❌ CODEF 실패 응답 코드: {}", code);
			if ("CF-04000".equals(code)) {
				throw new IllegalArgumentException("은행 아이디/비밀번호 오류");
			} else {
				throw new RuntimeException("CODEF 응답 오류 : " + code);
			}
		}

		Map<String, Object> data = (Map<String, Object>)result.get("data");
		String connectedId = (String)data.get("connectedId");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		if (userEmail != null && connectedId != null) {
			log.info("✅ ConnectedId 저장 - userEmail: {}, connectedId: {}", userEmail, connectedId);
			userService.updateConnectedId(userEmail, connectedId);
			codefTokenService.saveAccountInfo(userEmail,connectedId);
			return ResponseEntity.ok(200);
		} else {
			log.error("❌ userEmail 또는 connectedId null - {}, {}", userEmail, connectedId);
			log.info(connectedId);
			log.info(result);
			throw new RuntimeException("서버 에러 : CODEF 에서 connectedId를 받아오지 못했거나 서버에서 유저의 email을 받아오지 못했습니다. " + code);
		}
	}

	// ✅ 계좌 목록 가져오고 DB 저장
	// @GetMapping("/account-info")
	// public ResponseEntity<?> getAccountInfo() {
	// 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// 	String user = authentication.getName();
	// 	String connectedId = userService.getUser(user).getConnectedId();
	// 	String organization = "0004";
	//
	// 	log.info("📥 계좌 정보 조회 요청: connectedId={}, organization={}", connectedId, organization);
	//
	// 	Map<String, Object> result = codefTokenService.getAccountInfo(connectedId, organization);
	// 	if (result == null || !result.containsKey("data")) {
	// 		log.error("❌ CODEF 계좌 응답 오류 또는 data 없음");
	// 		throw new RuntimeException("CODEF에서 계좌 정보를 받아올 수 없습니다.");
	// 	}
	//
	// 	Map<String, Object> data = (Map<String, Object>)result.get("data");
	// 	List<Map<String, Object>> resDepositTrust = (List<Map<String, Object>>)data.get("resDepositTrust");
	//
	// 	if (resDepositTrust == null || resDepositTrust.isEmpty()) {
	// 		log.info("🔎 계좌는 조회 성공했으나 예금/신탁 내역이 없음");
	// 		return ResponseEntity.ok(result); // 200 OK + 내용 없음
	// 	}
	//
	// 	String userEmail = authentication.getName();
	//
	// 	log.info("💾 예금 자산 저장 - 사용자: {}, 계좌 수: {}", userEmail, resDepositTrust.size());
	//
	// 	for (Map<String, Object> account : resDepositTrust) {
	// 		try {
	// 			AssetDetailDto asset = new AssetDetailDto();
	// 			asset.setEmail(userEmail);
	// 			asset.setAssetCategoryCode("2"); // 예적금
	// 			asset.setAssetName((String)account.get("resAccountName")); // 통장 이름
	// 			asset.setAmount(Long.parseLong((String)account.get("resAccountBalance")));
	// 			asset.setRegisteredAt(new Date());
	// 			asset.setEndDate(null);
	// 			asset.setBusinessType(null);
	//
	// 			assetDetailService.saveAssetDetail(asset);
	// 		} catch (Exception e) {
	// 			log.error("❗ 계좌 저장 실패: {}", e.getMessage(), e);
	// 			throw new RuntimeException("계좌 저장에 실패했습니다.");
	// 		}
	// 	}
	//
	// 	return ResponseEntity.ok(result);
	// }
}
