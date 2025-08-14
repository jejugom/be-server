package org.scoula.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 예약 테이블의 데이터를 집계하여 은행 서버로 전달하기 위해 생성된 BookingStatsDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatsDto {
	private Long branchId;
	private Long bookingCount;
}
