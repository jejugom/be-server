package org.scoula.product.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.scoula.product.domain.FundDailyReturnVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@AllArgsConstructor
public class FundDailyReturnDto {
	private String recordDate;
	private BigDecimal returnRate;

	// VO -> DTO 변환 메서드
	public static FundDailyReturnDto of(FundDailyReturnVo vo) {
		return new FundDailyReturnDto(
			vo.getRecordDate().format(DateTimeFormatter.ISO_DATE), // "yyyy-MM-dd"
			vo.getReturnRate()
		);
	}
}
