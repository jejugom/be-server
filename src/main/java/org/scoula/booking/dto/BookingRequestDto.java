package org.scoula.booking.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.scoula.booking.domain.BookingVo;

import lombok.Data;

@Data
public class BookingRequestDto {
	private String branchName;
	private String prodCode;
	private String date;
	private String time;

	/**
	 * DTO를 Vo로 변환합니다.
	 * email, docInfo, bookingUlid 등은 서비스 계층에서 별도로 설정합니다.
	 * @return BookingVo 객체
	 */
	public BookingVo toVo() {
		Date parsedDate;
		try {
			parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.date);
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd.", e);
		}

		return BookingVo.builder()
			.branchName(this.branchName)
			.prodCode(this.prodCode)
			.date(parsedDate)
			.time(this.time)
			.build();
	}
}
