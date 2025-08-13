package org.scoula.statistics.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.statistics.dto.BookingStatsDto;

@Mapper
public interface BookingStatsMapper {
	List<BookingStatsDto> selectBookingStatsBetween(Map<String, Object> params);
}
