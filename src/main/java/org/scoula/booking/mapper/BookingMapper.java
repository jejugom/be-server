package org.scoula.booking.mapper;

import org.scoula.booking.domain.BookingVO;

import java.util.List;

public interface BookingMapper {
    List<BookingVO> getBookingsByEmail(String email);
    BookingVO getBookingById(Integer id);
    void insertBooking(BookingVO booking);
    int updateBooking(BookingVO booking);
    int deleteBooking(Integer id);
}
