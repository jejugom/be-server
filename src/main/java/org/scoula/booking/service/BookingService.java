package org.scoula.booking.service;

import java.util.List;

import org.scoula.booking.dto.BookingCheckResponseDto;
import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;

public interface BookingService {
	List<BookingDto> getBookingsByEmail(String email);

	BookingCreateResponseDto addBooking(String email, BookingCreateRequestDto requestDto);

	void updateBooking(BookingDto bookingDto);

	void deleteBooking(Integer bookingId);

	BookingDetailResponseDto getBookingById(String bookingId);

	ReservedSlotsResponseDto getReservedSlotsByBranch(int branchId);

	public BookingCheckResponseDto checkBookingExists(String email, String prdtCode);
}
