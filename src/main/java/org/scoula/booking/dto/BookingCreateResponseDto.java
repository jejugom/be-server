package org.scoula.booking.dto;

import org.scoula.booking.domain.BookingVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "BookingCreateResponseDto", description = "예약 생성 응답 DTO")
public class BookingCreateResponseDto {

	@ApiModelProperty(value = "예약 고유 ID (ULID)", example = "01HXYZABCDEF12345678")
	private String bookingId;

	@ApiModelProperty(value = "예약에 필요한 서류 정보")
	private DocInfoDto docInfo;

	public static BookingCreateResponseDto of(BookingVo booking) {
		return BookingCreateResponseDto.builder()
			.bookingId(booking.getBookingId())
			.docInfo(booking.getDocInfo())
			.build();
	}
}
