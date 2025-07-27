package org.scoula.booking.dto;

import java.text.SimpleDateFormat;

import org.scoula.booking.domain.BookingVo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingCheckDetailDto {
	private String bookingUlid;
	private String date;
	private String time;
	private String branchName;

	public static BookingCheckDetailDto from(BookingVo booking) {
		String formattedDate = null;
		if (booking.getDate() != null) {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(booking.getDate());
		}

		String formattedTime = null;
		if (booking.getTime() != null && booking.getTime().length() >= 5) {
			formattedTime = booking.getTime().substring(0, 5);
		}

		return BookingCheckDetailDto.builder()
			.bookingUlid(booking.getBookingUlid())
			.date(formattedDate)
			.time(formattedTime)
			.branchName(booking.getBranchName())
			.build();
	}
}

