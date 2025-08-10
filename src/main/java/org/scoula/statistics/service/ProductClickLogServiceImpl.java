package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.scoula.statistics.domain.ProductClickLogVo;
import org.scoula.statistics.dto.ProductClickStatsDto;
import org.scoula.statistics.mapper.ProductClickLogMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductClickLogServiceImpl implements ProductClickLogService {
	private final ProductClickLogMapper logMapper;

	public void saveClickLog(String finPrdtCd, String email, String triggeredBy) {
		ProductClickLogVo log = new ProductClickLogVo();
		log.setFinPrdtCd(finPrdtCd);
		log.setEmail(email);
		log.setTriggeredBy(triggeredBy);
		logMapper.insertClickLog(log);
	}

	@Override
	public List<ProductClickStatsDto> getClickStatsSince(LocalDateTime fromDate) {
		return logMapper.selectClickStatsSince(fromDate);
	}
}
