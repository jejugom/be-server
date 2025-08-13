package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;

public interface ProductClickLogService {
	void saveClickLog(String finPrdtCd, String email, String triggeredBy);
	List<ProductClickStatsDto> getClickStatsSince(LocalDateTime fromDate);
}
