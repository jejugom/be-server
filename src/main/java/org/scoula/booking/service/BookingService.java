package org.scoula.booking.service;

import java.util.List;

import org.scoula.booking.dto.BookingDto;

public interface BookingService {
	List<BookingDto> getBookingsByEmail(String email);

	BookingDto getBookingById(Integer bookingId);

	void addBooking(BookingDto bookingDto);

	void updateBooking(BookingDto bookingDto);

	void deleteBooking(Integer bookingId);
}
