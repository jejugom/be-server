package org.scoula.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCheckResponseDto {

	private boolean exists;
	private BookingCheckDetailDto bookingDetails;
}
