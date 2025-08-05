package org.scoula.View.codef.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

/**
 * CODEF API와의 통신을 관리하는 서비스 클래스. Access Token 발급 및 관리, ConnectedId 생성, 계좌 정보 조회 및 저장을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class CodefTokenService {

  private final CodefApiClient codefApiClient;
  private final AssetStatusService assetStatusService;
  private final UserService userService;

  private static final String LOG_PATH = "/Users/wina/Documents/github/nohudorak/be-server/logs/codef_log.csv";

  // TODO: Access Token을 안전하게 저장하고 관리하는 로직 추가 필요
  /**
   * CODEF API Access Token
   */
  private String accessToken;

  /**
   * Access Token의 만료 시간을 저장 (Unix 타임스탬프, 밀리초)
   */
  private long tokenExpiryTime;

  /**
   * 의존성 주입 후 초기화 작업을 수행합니다. CodefApiClient에 현재 서비스의 인스턴스를 설정하여 상호 참조를 해결합니다.
   */
  @PostConstruct
  public void init() {
    codefApiClient.setCodefTokenService(this);
  }

  /**
   * 유효한 CODEF Access Token을 반환합니다. 토큰이 없거나 만료된 경우, 자동으로 새로운 토큰을 발급받아 갱신합니다.
   *
   * @return 유효한 Access Token 문자열
   */
  public String getAccessToken() {
    // TODO: 토큰 만료 여부 확인 및 갱신 로직 추가
    if (accessToken == null || isTokenExpired()) {
      log.info("Access Token is null or expired. Publishing new token...");
      Map<String, Object> tokenMap = codefApiClient.publishToken();
      if (tokenMap != null && tokenMap.containsKey("access_token")) {
        this.accessToken = (String) tokenMap.get("access_token");
        // 토큰 유효 기간 (초)
        int expiresIn = (Integer) tokenMap.get("expires_in");
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

  /**
   * 현재 저장된 Access Token이 만료되었는지 확인합니다.
   *
   * @return 만료되었으면 true, 아니면 false
   */
  private boolean isTokenExpired() {
    // 만료 시간보다 현재 시간이 크면 토큰 만료
    return System.currentTimeMillis() >= tokenExpiryTime;
  }

  /**
   * 사용자의 금융사 계정 정보를 바탕으로 CODEF ConnectedId를 생성합니다. 이 과정에서 계정 비밀번호를 암호화하여 전송합니다.
   *
   * @param requestDto ConnectedId 생성에 필요한 계정 정보 목록
   * @return CODEF API로부터 반환된 결과 맵
   */
  public Map<String, Object> createConnectedId(ConnectedIdRequestDto requestDto) {
    try {
      long start = System.currentTimeMillis();

      // 리스트 내부의 account 객체 수정 (id/password 암호화)
      for (Map<String, Object> account : requestDto.getAccountList()) {
        if (account.containsKey("password")) {
          String encryptedPassword = codefApiClient.encryptRSA((String) account.get("password"),
                  codefApiClient.getPublicKey())
              .replaceAll("\n", "");
          account.put("password", encryptedPassword);
        }
        log.info("🔐 암호화 후 account: {}", account); // ✅ 추가
      }

      // 계정 리스트 통째로 담은 요청
      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("accountList", requestDto.getAccountList());
      Map<String, Object> response = codefApiClient.createConnectedId(bodyMap);

      long end = System.currentTimeMillis();
      long duration = end - start;

      log.info("⏱️ ConnectedId 생성 완료, 소요 시간: {}ms", duration);

//성공 여부 파악
      String code = null;
      if (response != null && response.get("result") != null) {
        code = (String) ((Map<String, Object>) response.get("result")).get("code");
      }

      logCodefExecution("createConnectedId", duration, code);

      return response;

    } catch (Exception e) {
      log.error("Error while creating ConnectedId: {}", e.getMessage(), e);
      return null;
    }
  }

  /**
   * 발급받은 ConnectedId를 사용하여 사용자의 계좌 정보를 조회하고, 해당 정보를 시스템의 자산 현황으로 저장합니다.
   *
   * @param userEmail   자산 정보를 저장할 사용자의 이메일
   * @param connectedId 계좌 정보 조회를 위한 ConnectedId
   */
  public void saveAccountInfo(String userEmail, String connectedId) {
    String organization = "0004"; // 은행 코드 (0004는 예시)
    log.info("📥 계좌 정보 조회 요청: connectedId={}, organization={}", connectedId, organization);

    Map<String, Object> result = getAccountInfo(connectedId, organization);
    if (result == null || !result.containsKey("data")) {
      log.error("❌ CODEF 계좌 응답 오류 또는 data 없음");
      throw new RuntimeException("CODEF에서 계좌 정보를 받아올 수 없습니다.");
    }

    Map<String, Object> data = (Map<String, Object>) result.get("data");
    List<Map<String, Object>> resDepositTrust = (List<Map<String, Object>>) data.get(
        "resDepositTrust");

    if (resDepositTrust == null || resDepositTrust.isEmpty()) {
      log.info("🔎 계좌는 조회 성공했으나 예금/신탁 내역이 없음");
      return; // 200 OK + 내용 없음
    }

    log.info("💾 예금 자산 저장 - 사용자: {}, 계좌 수: {}", userEmail, resDepositTrust.size());

    for (Map<String, Object> account : resDepositTrust) {
      try {
        // 자산 현황 DTO를 생성하여 서비스에 저장 요청
        AssetStatusRequestDto asset = new AssetStatusRequestDto();
        asset.setAssetCategoryCode("2"); // 예적금
        asset.setAssetName((String) account.get("resAccountName")); // 통장 이름
        asset.setAmount(Long.parseLong((String) account.get("resAccountBalance")));
        asset.setBusinessType(null);
        assetStatusService.addAssetStatus(userEmail, asset);

        /**
         * 사용자 총 자산에 Codef에서 불러온 계좌 자산 금액 추가
         */
        UserDto userDto = userService.getUser(userEmail);
        Long curBalance = userDto.getAsset();
        curBalance += Long.parseLong((String) account.get("resAccountBalance"));
        userDto.setAsset(curBalance);
        userService.updateUser(userEmail, userDto);

      } catch (Exception e) {
        log.error("❗ 계좌 저장 실패: {}", e.getMessage(), e);
        throw new RuntimeException("계좌 저장에 실패했습니다.");
      }
    }
  }

  /**
   * CodefApiClient를 통해 특정 기관의 계좌 정보를 조회합니다.
   *
   * @param connectedId  조회할 사용자의 ConnectedId
   * @param organization 조회할 기관 코드
   * @return CODEF API로부터 반환된 결과 맵
   */
  public Map<String, Object> getAccountInfo(String connectedId, String organization) {
    return codefApiClient.getAccountInfo(connectedId, organization);
  }

  private void logCodefExecution(String apiName, long duration, String resultCode) {
    String logLine = String.format("%s,%s,%d,%s\n",
        LocalDateTime.now(),
        apiName,
        duration,
        resultCode != null ? resultCode : "UNKNOWN"
    );
    log.info("📊 CODEF 실행 로그: {}", logLine);

    try {
      Files.createDirectories(Paths.get(LOG_PATH).getParent()); // 디렉토리 없으면 생성
      try (PrintWriter out = new PrintWriter(new FileWriter(LOG_PATH, true))) {
        out.write(logLine);
      }
    } catch (IOException e) {
      log.error("🔴 CODEF 로그 저장 실패", e);
    }
  }
}