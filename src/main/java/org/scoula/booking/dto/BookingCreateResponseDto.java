package org.scoula.booking.dto;

import org.scoula.booking.domain.BookingVo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingCreateResponseDto {
	private String bookingId;      // 고유 ID (ULID)
	private DocInfoDto docInfo;    // 필요 서류 정보

	public static BookingCreateResponseDto of(BookingVo booking) {
		return BookingCreateResponseDto.builder()
			.bookingId(booking.getBookingId())
			.docInfo(booking.getDocInfo())
			.build();
	}
}
