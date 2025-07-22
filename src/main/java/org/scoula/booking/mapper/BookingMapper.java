package org.scoula.booking.mapper;

import java.util.List;

import org.scoula.booking.domain.BookingVo;

public interface BookingMapper {
	List<BookingVo> getBookingsByEmail(String email);

	BookingVo getBookingById(Integer id);

	void insertBooking(BookingVo booking);

	int updateBooking(BookingVo booking);

	int deleteBooking(Integer id);
}
