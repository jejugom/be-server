package org.scoula.statistics.service;

import java.time.LocalDateTime;
import java.util.Date;

public interface ClickStatsSendHistoryService {
	LocalDateTime findLastSentAt();
	void insertSentAt(LocalDateTime sentAt);
}
