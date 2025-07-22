package org.scoula.booking.controller;

import java.util.List;

import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	@GetMapping("/user/{email}")
	public ResponseEntity<List<BookingDto>> getBookingsByEmail(@PathVariable String email) {
		return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingDto> getBookingById(@PathVariable Integer bookingId) {
		return ResponseEntity.ok(bookingService.getBookingById(bookingId));
	}

	@PostMapping
	public ResponseEntity<Void> addBooking(@RequestBody BookingDto bookingDto) {
		bookingService.addBooking(bookingDto);
		return ResponseEntity.ok().build();
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
