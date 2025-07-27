package org.scoula.booking.dto;

import org.scoula.booking.domain.BookingVo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponseDto {
	private String bookingId;      // 외부용 고유 ID (ULID)
	private DocInfoDto docInfo;    // 필요 서류 정보

	public static BookingResponseDto of(BookingVo booking) {
		return BookingResponseDto.builder()
			.bookingId(booking.getBookingUlid())
			.docInfo(booking.getDocInfo())
			.build();
	}
}
