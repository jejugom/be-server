package org.scoula.statistics.service;

import java.time.LocalDateTime;

public interface StatsSendHistoryService {
	LocalDateTime findLastSentAt(String statType);
	void insertSentAt(String statType, LocalDateTime sentAt);
}
