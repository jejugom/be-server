package org.scoula.booking.controller;

import java.net.URI;
import java.util.List;

import org.scoula.booking.dto.BookingCheckResponseDto;
import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;
import org.scoula.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	 * @param bookingUlid 외부 예약 번호
	 * */
	@GetMapping("/detail/{bookingUlid}")
	public ResponseEntity<BookingDetailResponseDto> getBookingByUlid(@PathVariable String bookingUlid) {
		// 서비스로부터 BookingDetailResponseDto를 받아옴
		BookingDetailResponseDto responseDto = bookingService.getBookingByUlid(bookingUlid);

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
	@GetMapping("/{branchName}/reserved-slots")
	public ResponseEntity<ReservedSlotsResponseDto> getReservedSlots(@PathVariable String branchName) {
		ReservedSlotsResponseDto responseDto = bookingService.getReservedSlotsByBranch(branchName);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 사용자에 대해 특정 상품에 대한 예약 여부 조회
	 * */
	@GetMapping("/check/{prdt_code}")
	public ResponseEntity<BookingCheckResponseDto> checkBookingExists(
		@PathVariable("prdt_code") String prdtCode,
		Authentication authentication) {

		String email = authentication.getName();
		BookingCheckResponseDto responseDto = bookingService.checkBookingExists(email, prdtCode);

		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/user")
	public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
		String email = authentication.getName();

		List<BookingDto> bookings = bookingService.getBookingsByEmail(email);

		return ResponseEntity.ok(bookings);
	}

	@PutMapping("/{bookingId}")
	public ResponseEntity<Void> updateBooking(@PathVariable Integer bookingId, @RequestBody BookingDto bookingDto) {
		bookingDto.setBookingId(bookingId);
		bookingService.updateBooking(bookingDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<Void> deleteBooking(@PathVariable Integer bookingId) {
		bookingService.deleteBooking(bookingId);
		return ResponseEntity.ok().build();
	}
}
