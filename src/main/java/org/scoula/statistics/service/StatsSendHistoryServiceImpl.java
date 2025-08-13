package org.scoula.statistics.service;

import java.time.LocalDateTime;

import org.scoula.statistics.mapper.StatsSendHistoryMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsSendHistoryServiceImpl implements StatsSendHistoryService {

	private final StatsSendHistoryMapper sendHistoryMapper;

	@Override
	public LocalDateTime findLastSentAt(String statType) {
		return sendHistoryMapper.findLastSentAt(statType);
	}

	@Override
	public void insertSentAt(String statType, LocalDateTime sentAt) {
		sendHistoryMapper.insertSentAt(statType, sentAt);
	}
}
