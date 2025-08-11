package org.scoula.statistics.domain;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 집계 데이터 전송 기록 관련 VO(Value Object) 클래스
 */
@ApiModel(value = "집계 데이터 전송 기록 VO", description = "DB의 stats_send_history 테이블과 매핑되는 핵심 객체")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsSendHistoryVo {
	private Long id;
	private String statType;
	private LocalDateTime sentAt;
}
