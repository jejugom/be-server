package org.scoula.statistics.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;
import org.scoula.statistics.mapper.ProductClickLogMapper;
import org.scoula.statistics.service.BankServerApiClient;
import org.scoula.statistics.service.StatsSendHistoryService;
import org.scoula.statistics.service.ProductClickStatsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductClickStatsScheduler {

	private final ProductClickLogMapper logMapper;
	private final BankServerApiClient bankServerApiClient;
	private final StatsSendHistoryService sendHistoryService;
	private final ProductClickStatsService clickStatsService;

	@Transactional
	@Scheduled(cron = "0 * * * * *") // 예시
	public void sendClickStats() {
		LocalDateTime lastSentAt = sendHistoryService.findLastSentAt("CLICK");
		if (lastSentAt == null) {
			lastSentAt = LocalDateTime.now().minusDays(1);
		}

		List<ProductClickStatsDto> stats = logMapper.selectClickStatsSince(lastSentAt);

		if (!stats.isEmpty()) {
			bankServerApiClient.sendClickStats(stats);
			sendHistoryService.insertSentAt("CLICK", LocalDateTime.now());
			log.info("클릭 집계 데이터 {}건 전송 완료", stats.size());
		} else {
			log.info("클릭 집계 데이터 없음");
		}
	}

	@Transactional
	@Scheduled(cron = "0 0 0 1 * *") // 매월 1일 자정 실행
	public void deleteOldLogs() {
		LocalDateTime threshold = LocalDateTime.now().minusMonths(1);
		logMapper.deleteClickLogsBefore(threshold);
	}

}
