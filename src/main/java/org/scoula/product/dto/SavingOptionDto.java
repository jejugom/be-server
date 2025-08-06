package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SavingOptionDto {

	private String saveTrm; //저축 기간[개월]
	private String rsrvTypeNm; //적립 유형명
	private String intrRateTypeNm; //저축금리 유형명
	private String intrRate; //저축금리
	private String intrRate2; //최고 우대금리
	private Double tendency; //투자성향
}
