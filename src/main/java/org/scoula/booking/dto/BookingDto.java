package org.scoula.booking.dto;

import java.util.Date;

import org.scoula.booking.domain.BookingVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
	private String bookingId;
	private String email;
	private String finPrdtCode;
	private Date date;
	private String time;
	private DocInfoDto docInfo;
	private String userEmail;

	public static BookingDto of(BookingVo booking) {
		return BookingDto.builder()
			.bookingId(booking.getBookingId())
			.email(booking.getEmail())
			.finPrdtCode(booking.getFinPrdtCode())
			.date(booking.getDate())
			.time(booking.getTime())
			.docInfo(booking.getDocInfo())
			.build();
	}

	public BookingVo toVo() {
		return BookingVo.builder()
			.bookingId(bookingId)
			.email(email)
			.finPrdtCode(finPrdtCode)
			.date(date)
			.time(time)
			.docInfo(docInfo)
			.build();
	}
}
