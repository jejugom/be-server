package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SavingOptionDto extends DepositSavingOptionDto {

	private String rsrvTypeNm; //적립 유형명
	private String intrRateTypeNm; //저축금리 유형명
}
