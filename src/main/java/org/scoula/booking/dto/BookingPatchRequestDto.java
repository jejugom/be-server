package org.scoula.booking.dto;

import lombok.Data;

@Data
public class BookingPatchRequestDto {
	// 사용자가 변경할 수 있는 필드만 정의합니다.
	private String date;
	private String time;
}
