package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoldProductDetailDto {

	private String finPrdtCd; // 상품코드
	private String korCoNm; // 금융회사명
	private String finPrdtCategory; // 금융카테고리
	private String finPrdtNm; // 상품명
	private String prdtFeature; // 상품특징
	private String description; // 상품설명
	private String joinWay; // 가입방법
	private String mtrtInt; // 만기 후 이자율 (이자에 관한 것으로 적용 예정)
	private String lot; // 거래단위
	private String currency; // 거래통화
	private String joinMember; // 가입대상
	private String etcNote; // 기타 유의사항

}
