package org.scoula.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 상품 예약하기 클릭 통계 데이터를 집계하여 은행서버로 전송할 때 필요한 DTO 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductClickStatsDto {

	private String finPrdtCd;
	private Long clickCount;
}
