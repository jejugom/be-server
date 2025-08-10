package org.scoula.statistics.domain;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 클릭 로그 전달 히스토리 관련 VO(Value Object) 클래스
 */
@ApiModel(value = "클릭 로그 전달 히스토리 VO", description = "DB의 click_stats_send_history  테이블과 매핑되는 핵심 객체")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClickStatsSendHistoryVo {
	private Long id;
	private LocalDateTime sentAt;
	private LocalDateTime createdAt;
}
