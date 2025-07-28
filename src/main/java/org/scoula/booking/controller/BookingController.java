package org.scoula.booking.controller;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.scoula.booking.dto.BookingCheckResponseDto;
import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingPatchRequestDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;
import org.scoula.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	/**
	 * 예약 번호로 예약 상세 조회하기
	 *
	 * @param bookingId 예약 번호
	 */
	@GetMapping("/detail/{bookingId}")
	public ResponseEntity<BookingDetailResponseDto> getBookingById(@PathVariable String bookingId) {
		// 서비스로부터 BookingDetailResponseDto를 받아옴
		BookingDetailResponseDto responseDto = bookingService.getBookingById(bookingId);

		// 최종 DTO를 클라이언트에게 응답
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 예약 생성하기
	 * */
	@PostMapping // "/bookings" 경로에 대한 POST 요청 처리
	public ResponseEntity<BookingCreateResponseDto> addBooking( // 반환 타입을 BookingResponseDto로 변경
		Authentication authentication,
		@RequestBody BookingCreateRequestDto requestDto) { // 받는 타입을 BookingRequestDto로 변경

		String email = authentication.getName();

		// 서비스는 완성된 BookingResponseDto를 반환
		BookingCreateResponseDto responseDto = bookingService.addBooking(email, requestDto);

		// 생성된 리소스의 URI를 생성
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(responseDto.getBookingId()) // String ID 사용
			.toUri();

		// 201 Created 응답과 함께 생성된 리소스(ResponseDto)를 본문에 담아 반환
		return ResponseEntity.created(location).body(responseDto);
	}

	/**
	 * 특정 지점 예약 목록 조회
	 * */
	@GetMapping("/{branchId}/reserved-slots")
	public ResponseEntity<ReservedSlotsResponseDto> getReservedSlots(@PathVariable Integer branchId) {
		ReservedSlotsResponseDto responseDto = bookingService.getReservedSlotsByBranch(branchId);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 사용자에 대해 특정 상품에 대한 예약 여부 조회
	 * */
	@GetMapping("/check/{fin_prdt_code}")
	public ResponseEntity<BookingCheckResponseDto> checkBookingExists(
		@PathVariable("fin_prdt_code") String finPrdtCode,
		Authentication authentication) {

		String email = authentication.getName();
		BookingCheckResponseDto responseDto = bookingService.checkBookingExists(email, finPrdtCode);

		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/user")
	public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
		String email = authentication.getName();

		List<BookingDto> bookings = bookingService.getBookingsByEmail(email);

		return ResponseEntity.ok(bookings);
	}

	/**
	 * 예약 번호로 예약 정보 부분 수정하기 (날짜, 시간 등)
	 *
	 * @param bookingId 예약 번호
	 * @param patchDto  수정할 정보가 담긴 DTO
	 */
	@PatchMapping("/{bookingId}")
	public ResponseEntity<BookingDetailResponseDto> patchBooking(
		@PathVariable String bookingId,
		@RequestBody BookingPatchRequestDto patchDto,
		Authentication authentication) throws AccessDeniedException { // 👈 Principal 파라미터 추가

		// 1. Principal 객체에서 현재 사용자의 이메일을 가져옵니다.
		String email = authentication.getName();

		// 2. 서비스에 실제 사용자 이메일을 전달합니다.
		BookingDetailResponseDto updatedBooking = bookingService.patchBooking(bookingId, email, patchDto);

		// 3. 수정된 최종 DTO를 클라이언트에게 응답합니다.
		return ResponseEntity.ok(updatedBooking);
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<Void> deleteBooking(
		@PathVariable String bookingId, // 👈 타입을 String으로 변경
		Authentication authentication) throws AccessDeniedException {

		// 1. Authentication 객체에서 현재 사용자의 이메일을 가져옵니다.
		String currentUserEmail = authentication.getName();

		// 2. 서비스에 예약 ID와 사용자 이메일을 모두 전달합니다.
		bookingService.deleteBooking(bookingId, currentUserEmail);

		// 3. 성공적으로 삭제되었음을 의미하는 204 No Content 응답을 반환합니다.
		return ResponseEntity.noContent().build();
	}
}
