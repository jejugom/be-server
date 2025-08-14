package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Deposit(예금) 옵션 DTO
 */
@SuperBuilder
@Getter
public class DepositOptionDto extends DepositSavingOptionDto {

	private String intrRateTypeNm; //저축금리 유형명
}
