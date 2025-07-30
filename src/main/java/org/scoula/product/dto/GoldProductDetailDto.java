package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoldProductDetailDto {

	private String fin_prdt_cd; //상품코드
	private String kor_co_nm; //금융회사명
	private String fin_prdt_category; //금융카테고리
	private String fin_prdt_nm; //상품명
	private String prdt_feature; //상품특징
	private String description; //상품설명
	private String join_way; //가입방법
	private String mtrt_int; //만기 후 이자율인데 여기서는 이자에 관한 것으로 적용될 예정
	private String lot; // 거래단위
	private String currency; //거대통화
	private String join_member; //가입대상
	private String etc_note; //기타 유의사항

}
