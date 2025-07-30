package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDepositsDetailDto {
	private String finPrdtCd; //금융 상품 코드
	private String finPrdtCategory; //금융 상품 카테고리
	private String finPrdtNm; //금융 상품명
	private String recReason; //추천 사유
	private String korCoNm; //금융회사명
	private String prdtFeature; //상품 특징
	private String description; //상품 설명
	private String joinWay; //가입 방법
	private String mtrtInt; //만기 후 이자율
	private String spclCnd; //우대조건
	private String joinDeny; //가입 제한 (join_deny) - 1:제한없음, 2:서민전용, 3:일부제한
	private String joinMember; //가입 대상
	private String etcNote; //기타 유의사항
	private String maxLimit; //최고 한도
	private Double tendency; //투자 성향

	private List<DetailOptionList> optionList; // 옵션 목록

	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class DetailOptionList { //OptionList로 이름 다시 변경해야함
		private String intrRateTypeNm;
		private String rsrvTypeNm;
		private String saveTrm;    // 저축 기간 [단위: 개월]
		private Double intrRate;    // 금리
		private Double intrRate2;    // 최고 우대금리
	}
}
