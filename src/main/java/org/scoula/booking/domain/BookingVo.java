package org.scoula.booking.domain;

import java.util.Date;

import org.scoula.booking.dto.DocInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingVo {
	private String bookingId;
	private int branchId;
	private String email;
	private String finPrdtCode;
	private Date date;
	private String time;
	private DocInfoDto docInfo;
}
