package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;
import org.scoula.statistics.mapper.ProductClickLogMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductClickStatsServiceImpl implements ProductClickStatsService {

	private final ProductClickLogMapper logMapper;
	private final BankServerApiClient bankApiClient;

	@Override
	public void sendStatsToBank() {
		// 1일 전부터 현재까지 집계
		LocalDateTime fromDate = LocalDateTime.now().minusDays(1);

		List<ProductClickStatsDto> stats = getClickStatsSince(fromDate);

		if (!stats.isEmpty()) {
			bankApiClient.sendClickStats(stats);
		}
	}

	@Override
	public List<ProductClickStatsDto> getClickStatsSince(LocalDateTime fromDate) {
		return logMapper.selectClickStatsSince(fromDate);
	}

	@Override
	public void deleteClickLogsBefore(LocalDateTime toDate) {
		logMapper.deleteClickLogsBefore(toDate);
	}
}
