package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;

public interface ProductClickStatsService {

	/**
	 * 일정 기간 이후 클릭 로그를 집계해서 은행 서버로 전송하는 메서드
	 */
	void sendStatsToBank();

	/**
	 * 특정 시점 이후의 클릭 로그 통계 집계 데이터 조회
	 * @param fromDate 조회 시작 날짜
	 * @return 클릭 통계 리스트
	 */
	List<ProductClickStatsDto> getClickStatsSince(LocalDateTime fromDate);

	void deleteClickLogsBefore(LocalDateTime toDate);
}
