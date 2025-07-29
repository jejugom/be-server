package org.scoula.booking.dto;

import java.util.Date;

import org.scoula.booking.domain.BookingVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "BookingDto", description = "예약 정보 전체를 나타내는 DTO")
public class BookingDto {

	@ApiModelProperty(value = "예약 고유 ID (ULID)", example = "01HXYZABCDEF12345678")
	private String bookingId;

	@ApiModelProperty(value = "예약자 이메일", example = "user@example.com")
	private String email;

	@ApiModelProperty(value = "금융 상품 코드", example = "FIN123456")
	private String finPrdtCode;

	@ApiModelProperty(value = "예약 날짜", example = "2025-07-28")
	private Date date;

	@ApiModelProperty(value = "예약 시간", example = "14:30")
	private String time;

	@ApiModelProperty(value = "필요 서류 정보")
	private DocInfoDto docInfo;

	@ApiModelProperty(value = "사용자 이메일 (예비용)", example = "user@example.com")
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
