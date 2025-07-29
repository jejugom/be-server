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

	@ApiModelProperty(value = "지점 번호", example = "1")
	private int branchId;

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

	/**
	 * BookingVo 객체를 BookingDto로 변환합니다.
	 * @param booking 변환할 BookingVo 객체
	 * @return 변환된 BookingDto 객체
	 */
	public static BookingDto of(BookingVo booking) {
		return BookingDto.builder()
			.bookingId(booking.getBookingId())
			.branchId(booking.getBranchId()) // branchId 필드 매핑 추가
			.email(booking.getEmail())
			.finPrdtCode(booking.getFinPrdtCode())
			.date(booking.getDate())
			.time(booking.getTime())
			.docInfo(booking.getDocInfo())
			.build();
	}

	/**
	 * BookingDto 객체를 BookingVo로 변환합니다.
	 * @return 변환된 BookingVo 객체
	 */
	public BookingVo toVo() {
		return BookingVo.builder()
			.bookingId(bookingId)
			.branchId(branchId) // branchId 필드 매핑 추가
			.email(email)
			.finPrdtCode(finPrdtCode)
			.date(date)
			.time(time)
			.docInfo(docInfo)
			.build();
	}
}