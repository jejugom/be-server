package org.scoula.statistics.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;
import org.scoula.statistics.mapper.ProductClickLogMapper;
import org.scoula.statistics.service.BankServerApiClient;
import org.scoula.statistics.service.ClickStatsSendHistoryService;
import org.scoula.statistics.service.ProductClickStatsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductClickStatsSchedulerImpl implements ProductClickStatsScheduler {

	private final ProductClickLogMapper logMapper;
	private final BankServerApiClient bankServerApiClient;
	private final ClickStatsSendHistoryService sendHistoryService;
	private final ProductClickStatsService clickStatsService;

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * SUN") // 매일 자정 실행
	public void sendClickStats() {
		LocalDateTime lastSentAt = sendHistoryService.findLastSentAt();
		if (lastSentAt == null) {
			// 최초 실행 시 1일 전부터 집계
			lastSentAt = LocalDateTime.now().minusDays(1);
		}

		List<ProductClickStatsDto> stats = logMapper.selectClickStatsSince(lastSentAt);

		if (!stats.isEmpty()) {
			bankServerApiClient.sendClickStats(stats);
			log.info("집계된 데이터 {}건 전송 완료", stats.size());

			sendHistoryService.insertSentAt(LocalDateTime.now());
		} else {
			log.info("집계 데이터 없음");
		}
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 1 * *") // 매월 1일 자정 실행
	public void deleteOldLogs() {
		LocalDateTime threshold = LocalDateTime.now().minusMonths(1);
		logMapper.deleteClickLogsBefore(threshold);
	}

}
