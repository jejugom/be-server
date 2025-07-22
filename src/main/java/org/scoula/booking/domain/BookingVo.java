package org.scoula.booking.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingVo {
	private int bookingId;
	private String email;
	private String prdtCode;
	private Date date;
	private String time;
	private String docInfo;
	private String branchName;
}
