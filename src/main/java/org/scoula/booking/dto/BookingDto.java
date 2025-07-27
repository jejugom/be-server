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
	private int bookingId;
	private String email;
	private String prodCode;
	private Date date;
	private String time;
	private DocInfoDto docInfo;
	private String branchName;
	private String userEmail;

	public static BookingDto of(BookingVo booking) {
		return BookingDto.builder()
			.bookingId(booking.getBookingId())
			.email(booking.getEmail())
			.prodCode(booking.getProdCode())
			.date(booking.getDate())
			.time(booking.getTime())
			.docInfo(booking.getDocInfo())
			.branchName(booking.getBranchName())
			.build();
	}

	public BookingVo toVo() {
		return BookingVo.builder()
			.bookingId(bookingId)
			.email(email)
			.prodCode(prodCode)
			.date(date)
			.time(time)
			.docInfo(docInfo)
			.branchName(branchName)
			.build();
	}
}
