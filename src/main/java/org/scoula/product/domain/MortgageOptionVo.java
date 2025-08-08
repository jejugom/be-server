package org.scoula.product.domain;

import lombok.Data;

@Data
public class MortgageOptionVo {

	private Integer optionCd; //옵션코드(id역할)
	private String mrtgTypeNm; //담보유형
	private String rpayTypeNm; //대출상환유형
	private String lendRateTypeNm; //대출금리유형
	private Double lendRateMin; //대출금리 최저
	private Double lendRateMax; //대출금리 최고
	private Double lendRateAvg; //전월 취급 평균금리
}
