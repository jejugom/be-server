package org.scoula.statistics.scheduler;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.scoula.statistics.dto.BookingStatsDto;
import org.scoula.statistics.service.BankServerApiClient;
import org.scoula.statistics.service.BookingStatsService;
import org.scoula.statistics.service.StatsSendHistoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingStatsScheduler {

	private final BookingStatsService bookingStatsService;
	private final BankServerApiClient bankServerApiClient;
	private final StatsSendHistoryService historyService;

	@Transactional
	@Scheduled(cron = "0 0 0 1 * *") // 매월 1일 00시
	public void sendBookingStats() {
		LocalDateTime startOfLastMonth = YearMonth.now()
			.minusMonths(1)
			.atDay(1)
			.atStartOfDay();

		LocalDateTime endOfLastMonth = YearMonth.now()
			.minusMonths(1)
			.atEndOfMonth()
			.atTime(23, 59, 59);

		List<BookingStatsDto> stats = bookingStatsService.getBookingStatsBetween(startOfLastMonth, endOfLastMonth);

		if (!stats.isEmpty()) {
			bankServerApiClient.sendBookingStats(stats);
			historyService.insertSentAt("BOOKING", LocalDateTime.now());
		}
	}
}
