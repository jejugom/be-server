package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Deposit(예금)과 Saving(적금)에 동일하게 요구되는 필드를 모아둔 DTO
 */
@SuperBuilder
@ToString(callSuper = true)
@Getter
public class DepositSavingOptionDto {
	private String saveTrm; //저축 기간[개월]
	private String intrRate; //저축금리
	private String intrRate2; //최고 우대금리
}
