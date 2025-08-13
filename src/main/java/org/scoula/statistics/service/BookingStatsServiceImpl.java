package org.scoula.statistics.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scoula.statistics.dto.BookingStatsDto;
import org.scoula.statistics.mapper.BookingStatsMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingStatsServiceImpl implements BookingStatsService {

	private final BookingStatsMapper bookingStatsMapper;

	@Override
	public List<BookingStatsDto> getBookingStatsBetween(LocalDateTime from, LocalDateTime to) {
		Map<String, Object> params = new HashMap<>();
		params.put("fromDate", from);
		params.put("toDate", to);

		return bookingStatsMapper.selectBookingStatsBetween(params);
	}
}
