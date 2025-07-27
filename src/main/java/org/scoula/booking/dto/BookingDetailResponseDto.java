package org.scoula.booking.dto;

import java.text.SimpleDateFormat;

import org.scoula.booking.domain.BookingVo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDetailResponseDto {
	private String bookingId;      // 외부용 고유 ID (ULID)
	private String branchName;
	private String prodName;       // 상품명
	private String date;
	private String time;
	private DocInfoDto docInfo;

	/**
	 * BookingVo와 상품명을 조합하여 상세 응답 DTO를 생성합니다.
	 * @param booking Vo 객체
	 * @param prodName 서비스 계층에서 조회한 상품명
	 * @return 변환된 Dto 객체
	 */
	public static BookingDetailResponseDto of(BookingVo booking, String prodName) {

		String formattedDate = null;
		if (booking.getDate() != null) {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(booking.getDate());
		}

		return BookingDetailResponseDto.builder()
			.bookingId(booking.getBookingUlid())
			.branchName(booking.getBranchName())
			.prodName(prodName)
			.date(formattedDate)
			.time(booking.getTime())
			.docInfo(booking.getDocInfo())
			.build();
	}
}
