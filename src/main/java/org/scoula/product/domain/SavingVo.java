package org.scoula.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * vo.Saving(적금)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SavingVo extends ProductVo<SavingOptionVo> {

	private String mtrtInt; //만기 후 이자율
	private String spclCnd; //우대조건
	private String joinDeny; //가입제한 Ex) 1:제한없음, 2:서민전용, 3:일부제한
	private String joinMember; //가입대상
	private String joinPrice; //가입금액
	private String joinTerm; //가입기간
	private String etcNote; //기타 유의사항
}
