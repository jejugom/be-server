package org.scoula.booking.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.scoula.booking.dto.BookingCheckResponseDto;
import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingPatchRequestDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;

public interface BookingService {
	List<BookingDto> getBookingsByEmail(String email);

	BookingCreateResponseDto addBooking(String email, BookingCreateRequestDto requestDto);

	void deleteBooking(String bookingId, String currentUserEmail) throws AccessDeniedException;

	BookingDetailResponseDto getBookingById(String bookingId);

	ReservedSlotsResponseDto getReservedSlotsByBranch(int branchId);

	BookingCheckResponseDto checkBookingExists(String email, String prdtCode);

	/**
	 * 예약 정보를 부분 수정합니다 (날짜, 시간 등).
	 *
	 * @param bookingId 수정할 예약의 ID
	 * @param currentUserEmail 수정을 요청한 사용자의 이메일 (권한 확인용)
	 * @param patchDto 수정할 정보가 담긴 DTO
	 * @return 수정된 예약의 상세 정보 DTO
	 */
	BookingDetailResponseDto patchBooking(String bookingId, String currentUserEmail,
		BookingPatchRequestDto patchDto) throws
		AccessDeniedException;
}
