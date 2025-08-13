package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
public class FundSimpleOptionDto {
	private String rate3mon;       // 3개월 수익률
	private String riskGrade;      // 위험등급
	private String priceStd;       // 기준가
}
