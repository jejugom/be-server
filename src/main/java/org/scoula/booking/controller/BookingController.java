package org.scoula.booking.controller;

import java.net.URI;
import java.util.List;

import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingRequestDto;
import org.scoula.booking.dto.BookingResponseDto;
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

	@GetMapping("/user")
	public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
		String email = authentication.getName();

		List<BookingDto> bookings = bookingService.getBookingsByEmail(email);

		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingDto> getBookingById(@PathVariable Integer bookingId) {
		return ResponseEntity.ok(bookingService.getBookingById(bookingId));
	}

	@PostMapping // "/bookings" 경로에 대한 POST 요청 처리
	public ResponseEntity<BookingResponseDto> addBooking( // 반환 타입을 BookingResponseDto로 변경
		Authentication authentication,
		@RequestBody BookingRequestDto requestDto) { // 받는 타입을 BookingRequestDto로 변경

		String email = authentication.getName();

		// 서비스는 완성된 BookingResponseDto를 반환
		BookingResponseDto responseDto = bookingService.addBooking(email, requestDto);

		// 생성된 리소스의 URI를 생성
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(responseDto.getBookingId()) // String ID 사용
			.toUri();

		// 201 Created 응답과 함께 생성된 리소스(ResponseDto)를 본문에 담아 반환
		return ResponseEntity.created(location).body(responseDto);
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
