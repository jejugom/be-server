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
	private int bookingId;      // 내부용 Primary Key (PK)
	private String bookingUlid; // ⭐️ 외부용 고유 ID (ULID) 필드 추가
	private String email;
	private String prdtCode;
	private Date date;
	private String time;
	private DocInfoDto docInfo;
	private String branchName;
}
