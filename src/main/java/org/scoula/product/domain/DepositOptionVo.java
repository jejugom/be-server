package org.scoula.product.domain;

import lombok.Data;

@Data
public class DepositOptionVo {

	private Integer optionCd; //옵션코드(id역할)
	private String saveTrm; //저축 기간[개월]
	private String intrRateTypeNm; //저축금리 유형명
	private String intrRate; //저축금리
	private String intrRate2; //최고 우대금리
}
