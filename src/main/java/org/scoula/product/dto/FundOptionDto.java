package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundOptionDto {

	private String rate3mon;       // 3개월 수익률
	private String startDate;   // 시작일
	private String assetTotal;     // 총자산
	private String totalFee;       // 총보수
	private String riskGrade;      // 위험등급
	private String feeFirst;       // 선취수수료
	private String feeRedemp;      // 환매수수료
	private String priceStd;       // 기준가
	private Double tendency; //투자성향
}
