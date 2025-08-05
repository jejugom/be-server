package org.scoula.booking.dto;

import java.text.SimpleDateFormat;

import org.scoula.booking.domain.BookingVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "예약 상세 응답 DTO", description = "예약 상세 정보 조회 시 클라이언트에 반환하는 데이터")
public class BookingDetailResponseDto {

	@ApiModelProperty(value = "예약 고유 ID (ULID)", example = "01HXYZABCDEF12345678")
	private String bookingId;

	@ApiModelProperty(value = "지점명", example = "강남지점")
	private String branchName;

	@ApiModelProperty(value = "금융 상품명", example = "정기예금 1년제")
	private String prodName;

	@ApiModelProperty(value = "예약 날짜 (yyyy-MM-dd)", example = "2025-07-28")
	private String date;

	@ApiModelProperty(value = "예약 시간 (HH:mm)", example = "14:30")
	private String time;

	@ApiModelProperty(value = "필요 서류 정보")
	private DocInfoDto docInfo;

	/**
	 * BookingVo와 상품명을 조합하여 상세 응답 DTO를 생성합니다.
	 * @param booking Vo 객체
	 * @param prodName 서비스 계층에서 조회한 상품명
	 * @param branchName 지점명
	 * @return 변환된 Dto 객체
	 */
	public static BookingDetailResponseDto of(BookingVo booking, String prodName, String branchName) {
		String formattedDate = null;
		if (booking.getDate() != null) {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(booking.getDate());
		}

		return BookingDetailResponseDto.builder()
			.bookingId(booking.getBookingId())
			.branchName(branchName)
			.prodName(prodName)
			.date(formattedDate)
			.time(booking.getTime())
			.docInfo(booking.getDocInfo())
			.build();
	}
}
