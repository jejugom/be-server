package org.scoula.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class DepositOptionDto extends DepositSavingOptionDto {

	// private Integer optionCd; //옵션코드(id역할)
	private String intrRateTypeNm; //저축금리 유형명
}
