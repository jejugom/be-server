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

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addBooking(@RequestBody BookingDTO bookingDTO) {
        bookingService.addBooking(bookingDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBooking(@PathVariable Integer id, @RequestBody BookingDTO bookingDTO) {
        bookingDTO.setId(id);
        bookingService.updateBooking(bookingDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
