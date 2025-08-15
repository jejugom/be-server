package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Saving(적금) 옵션 DTO
 */
@SuperBuilder
@Getter
public class SavingOptionDto extends DepositSavingOptionDto {

	private String rsrvTypeNm; //적립 유형명
	private String intrRateTypeNm; //저축금리 유형명
}
