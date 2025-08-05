package org.scoula.View.preference.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.scoula.View.preference.dto.PreferenceRequestDto;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PreferenceService의 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

  private final UserService userService;
  private final CustomRecommendService customRecommendService;

  private static final String LOG_FILE_PATH = "/Users/wina/Documents/github/nohudorak/be-server/logs/recommend_log.csv";

  /**
   * 설문 답변을 점수화하여 사용자의 투자 성향(tendency)을 계산하고, 이를 바탕으로 사용자 정보를 업데이트한 후 맞춤 추천 상품 목록을 갱신합니다.
   *
   * @param requestDto 사용자의 설문 답변이 담긴 DTO
   * @param userEmail  성향을 설정할 사용자의 이메일
   * @implNote ⚠️ 주의: 현재 switch문에 break문이 없어 각 case가 연달아 실행(fall-through)됩니다. 예를 들어 q1이 1인 경우, 1, 2,
   * 3, 4번 case가 모두 실행되어 점수가 의도와 다르게 누적될 수 있습니다. 각 case가 독립적으로 실행되도록 하려면 각 case의 끝에 'break;'를 추가해야
   * 합니다.
   */
  @Override
  public void setUserPreference(PreferenceRequestDto requestDto, String userEmail) {
    LocalDateTime start = LocalDateTime.now();

    double score = calculateScore(requestDto);
    score = Math.max(-1, Math.min(1, score)); // -1 ~ 1 사이로 조정

    // 사용자 성향 업데이트
    UserDto userDto = userService.getUser(userEmail);
    userDto.setTendency(score);
    userService.updateUser(userEmail, userDto);

    // 추천 갱신
    customRecommendService.addCustomRecommend(userEmail);

    LocalDateTime end = LocalDateTime.now();
    long durationMillis = Duration.between(start, end).toMillis();

    // 로그 기록
    writeLog(start, "setUserPreference", durationMillis, userEmail, score);
  }

  private double calculateScore(PreferenceRequestDto dto) {
    double score = 0;
    score += switch (dto.getQ1()) {
      case 1 -> 0.3;
      case 2 -> 0.15;
      case 3 -> -0.15;
      case 4 -> -0.3;
      default -> 0;
    };
    score += switch (dto.getQ2()) {
      case 1 -> 0.3;
      case 2 -> 0;
      case 3 -> -0.3;
      default -> 0;
    };
    score += switch (dto.getQ3()) {
      case 1 -> 0.3;
      case 2 -> -0.15;
      case 3 -> -0.3;
      default -> 0;
    };
    score += switch (dto.getQ4()) {
      case 1 -> 0.3;
      case 2 -> 0.15;
      case 3 -> -0.15;
      case 4 -> -0.3;
      default -> 0;
    };
    score += switch (dto.getQ5()) {
      case 1 -> 0.3;
      case 2 -> 0;
      case 3 -> -0.3;
      default -> 0;
    };
    return score;
  }

  private void writeLog(LocalDateTime timestamp, String method, long durationMillis,
      String userEmail, double score) {
    try {
      File logFile = new File(LOG_FILE_PATH);
      File logDir = logFile.getParentFile();
      if (!logDir.exists()) {
        logDir.mkdirs();
      }

      boolean isNewFile = !logFile.exists();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
        if (isNewFile) {
          writer.write("timestamp,method,duration_ms,user_email,tendency_score\n");
        }
        String line = String.format("%s,%s,%d,%s,%.2f",
            timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            method,
            durationMillis,
            userEmail,
            score
        );
        writer.write(line + "\n");
      }
    } catch (IOException e) {
      System.err.println("로그 파일 기록 실패: " + e.getMessage());
    }
  }
}