package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.util.Date;

import org.scoula.statistics.mapper.ClickStatsSendHistoryMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClickStatsSendHistoryServiceImpl implements ClickStatsSendHistoryService {

	private final ClickStatsSendHistoryMapper sendHistoryMapper;

	@Override
	public LocalDateTime findLastSentAt() {
		return sendHistoryMapper.findLastSentAt();
	}

	@Override
	public void insertSentAt(LocalDateTime sentAt) {
		sendHistoryMapper.insertSentAt(sentAt);
	}
}
