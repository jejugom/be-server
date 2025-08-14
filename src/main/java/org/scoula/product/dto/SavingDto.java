package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Saving(적금) DTO
 */
@SuperBuilder
@ToString(callSuper = true)
@Getter
public class SavingDto extends ProductDetailDto<SavingOptionDto> {

	private String mtrtInt; //만기 후 이자율
	private String spclCnd; //우대조건
	private String joinDeny; //가입제한 Ex) 1:제한없음, 2:서민전용, 3:일부제한
	private String joinMember; //가입대상
	private String joinPrice; //가입금액
	private String joinTerm; //가입기간
	private String etcNote; //기타 유의사항
}
