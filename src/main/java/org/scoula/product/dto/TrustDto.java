package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
public class TrustDto extends ProductDetailDto<Object>{

	private Double basePrice; //기준가
	private Double yieldRate; //수익률
	private String fundType; //펀드유형
	private String fundStructure; //펀드형태
	private String taxBenefit; //세금우대
	private String saleStartDate; //판매시작일
	private String trustFee; //신탁보수
	private String earlyTerminationFee; //중도해지수수료
	private String depositProtection; //예금자보호 여부
}
