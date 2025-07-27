package org.scoula.booking.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.scoula.booking.domain.BookingVo;

public interface BookingMapper {
	List<BookingVo> getBookingsByEmail(String email);

	BookingVo getBookingById(Integer id);

	void insertBooking(BookingVo booking);

	int updateBooking(BookingVo booking);

	int deleteBooking(Integer id);

	// 특정 지점, 날짜, 시간에 해당하는 예약 건수를 세는 메서드 추가
	int countByBranchDateTime(@Param("branchName") String branchName, @Param("date") Date date,
		@Param("time") String time);

	BookingVo findByUlid(String bookingUlid);

	// 특정 지점의 현재 시각 기준으로 미래의 예약 목록을 조회하는 메서드
	List<BookingVo> findFutureByBranch(
		@Param("branchName") String branchName,
		@Param("startDate") Date startDate
	);
}
