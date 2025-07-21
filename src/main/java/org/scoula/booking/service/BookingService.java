package org.scoula.booking.service;

import org.scoula.booking.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    List<BookingDTO> getBookingsByEmail(String email);
    BookingDTO getBookingById(Integer bookingId);
    void addBooking(BookingDTO bookingDTO);
    void updateBooking(BookingDTO bookingDTO);
    void deleteBooking(Integer bookingId);
}
