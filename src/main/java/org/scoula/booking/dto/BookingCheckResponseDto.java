package org.scoula.booking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BookingCheckResponseDto", description = "예약 여부 및 상세 정보를 포함하는 응답 DTO")
public class BookingCheckResponseDto {

	@ApiModelProperty(value = "예약 존재 여부", example = "true")
	private boolean exists;

	@ApiModelProperty(value = "예약 상세 정보")
	private BookingCheckDetailDto bookingDetails;
}
