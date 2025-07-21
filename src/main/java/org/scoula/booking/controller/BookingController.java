package org.scoula.booking.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.booking.dto.BookingDTO;
import org.scoula.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<BookingDTO>> getBookingsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @PostMapping
    public ResponseEntity<Void> addBooking(@RequestBody BookingDTO bookingDTO) {
        bookingService.addBooking(bookingDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Void> updateBooking(@PathVariable Integer bookingId, @RequestBody BookingDTO bookingDTO) {
        bookingDTO.setBookingId(bookingId);
        bookingService.updateBooking(bookingDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok().build();
    }
}
