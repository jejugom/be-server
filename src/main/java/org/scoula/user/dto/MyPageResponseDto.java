package org.scoula.user.dto;

import java.util.List;

import org.scoula.booking.dto.BookingDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDto {
	private UserGraphDto userInfo; // 자산 정보를 포함하는 사용자 정보 DTO
	private List<BookingDto> bookingInfo; // 예약 내역 요약 리스트
}
