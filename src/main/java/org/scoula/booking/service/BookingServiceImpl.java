package org.scoula.booking.service;

import lombok.RequiredArgsConstructor;
import org.scoula.booking.dto.BookingDTO;
import org.scoula.booking.mapper.BookingMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    @Override
    public List<BookingDTO> getBookingsByEmail(String email) {
        return bookingMapper.getBookingsByEmail(email).stream()
                .map(BookingDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO getBookingById(Integer id) {
        return Optional.ofNullable(bookingMapper.getBookingById(id))
                .map(BookingDTO::of)
                .orElseThrow(() -> new NoSuchElementException("Booking not found with id: " + id));
    }

    @Override
    public void addBooking(BookingDTO bookingDTO) {
        bookingMapper.insertBooking(bookingDTO.toVO());
    }

    @Override
    public void updateBooking(BookingDTO bookingDTO) {
        if (bookingMapper.updateBooking(bookingDTO.toVO()) == 0) {
            throw new NoSuchElementException("Booking not found with id: " + bookingDTO.getId());
        }
    }

    @Override
    public void deleteBooking(Integer id) {
        if (bookingMapper.deleteBooking(id) == 0) {
            throw new NoSuchElementException("Booking not found with id: " + id);
        }
    }
}
