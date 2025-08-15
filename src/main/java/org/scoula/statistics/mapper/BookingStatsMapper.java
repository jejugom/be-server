package org.scoula.statistics.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.statistics.dto.BookingStatsDto;

/**
 * 집계한 예약 테이블에 관련된 Mapper
 */
@Mapper
public interface BookingStatsMapper {
	List<BookingStatsDto> selectBookingStatsBetween(Map<String, Object> params);
}
