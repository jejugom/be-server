package org.scoula.booking.service;

import java.util.List;

import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingRequestDto;
import org.scoula.booking.dto.BookingResponseDto;

public interface BookingService {
	List<BookingDto> getBookingsByEmail(String email);

	BookingDto getBookingById(Integer bookingId);

	BookingResponseDto addBooking(String email, BookingRequestDto requestDto);

	void updateBooking(BookingDto bookingDto);

	void deleteBooking(Integer bookingId);
}
