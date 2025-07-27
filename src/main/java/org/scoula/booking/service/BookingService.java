package org.scoula.booking.service;

import java.util.List;

import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;

public interface BookingService {
	List<BookingDto> getBookingsByEmail(String email);

	BookingDto getBookingById(Integer bookingId);

	BookingCreateResponseDto addBooking(String email, BookingCreateRequestDto requestDto);

	void updateBooking(BookingDto bookingDto);

	void deleteBooking(Integer bookingId);

	BookingDetailResponseDto getBookingByUlid(String bookingUlid);

	ReservedSlotsResponseDto getReservedSlotsByBranch(String branchName);
}
