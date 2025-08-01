package org.scoula.booking.dto;

import java.text.SimpleDateFormat;

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
@ApiModel(value = "예약 정보 DTO", description = "예약 정보 전체를 나타내는 객체")
public class BookingDto {

	@ApiModelProperty(value = "예약 고유 ID (ULID)", example = "01HXYZABCDEF12345678")
	private String bookingId;

	@ApiModelProperty(value = "지점 번호", example = "1")
	private int branchId;

	@ApiModelProperty(value = "금융 상품 코드", example = "FIN123456")
	private String finPrdtCode;

	@ApiModelProperty(value = "예약 날짜 (yyyy-MM-dd 형식)", example = "2025-07-28")
	private String date; // 타입을 Date에서 String으로 변경

	@ApiModelProperty(value = "예약 시간 (HH:mm 형식)", example = "14:30")
	private String time;

	@ApiModelProperty(value = "필요 서류 정보")
	private DocInfoDto docInfo;

	/**
	 * BookingVo 객체를 BookingDto로 변환하는 정적 팩토리 메소드입니다.
	 * 이 과정에서 날짜는 'yyyy-MM-dd', 시간은 'HH:mm' 형식으로 변환합니다.
	 * @param booking 변환할 BookingVo 객체
	 * @return 변환된 BookingDto 객체
	 */
	public static BookingDto of(BookingVo booking) {
		// 1. 날짜를 "yyyy-MM-dd" 형식의 문자열로 포맷팅
		String formattedDate = null;
		if (booking.getDate() != null) {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(booking.getDate());
		}

		// 2. 시간을 "HH:mm" 형식으로 포맷팅 (초 단위 제거)
		String originalTime = booking.getTime();
		String formattedTime = (originalTime != null && originalTime.length() > 5)
			? originalTime.substring(0, 5)
			: originalTime;

		// 3. 포맷팅된 값으로 DTO 빌드
		return BookingDto.builder()
			.bookingId(booking.getBookingId())
			.branchId(booking.getBranchId())
			.finPrdtCode(booking.getFinPrdtCode())
			.date(formattedDate) // 포맷팅된 날짜 사용
			.time(formattedTime) // 포맷팅된 시간 사용
			.docInfo(booking.getDocInfo())
			.build();
	}
}
