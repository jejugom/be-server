package org.scoula.booking.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.mapper.BookingMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingMapper bookingMapper;

	@Override
	public List<BookingDto> getBookingsByEmail(String email) {
		return bookingMapper.getBookingsByEmail(email).stream()
			.map(BookingDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public BookingDto getBookingById(Integer bookingId) {
		return Optional.ofNullable(bookingMapper.getBookingById(bookingId))
			.map(BookingDto::of)
			.orElseThrow(() -> new NoSuchElementException("Booking not found with id: " + bookingId));
	}

	@Override
	public void addBooking(BookingDto bookingDto) {
		bookingMapper.insertBooking(bookingDto.toVO());
	}

	@Override
	public void updateBooking(BookingDto bookingDto) {
		if (bookingMapper.updateBooking(bookingDto.toVO()) == 0) {
			throw new NoSuchElementException("Booking not found with id: " + bookingDto.getBookingId());
		}
	}

	@Override
	public void deleteBooking(Integer bookingId) {
		if (bookingMapper.deleteBooking(bookingId) == 0) {
			throw new NoSuchElementException("Booking not found with id: " + bookingId);
		}
	}
}
