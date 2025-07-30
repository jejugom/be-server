package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDepositsDetailDto {
	private String fin_prdt_cd; //금융 상품 코드
	private String fin_prdt_category; //금융 상품 카테고리
	private String fin_prdt_nm; //금융 상품명
	private String rec_reason; //추천 사유
	private String kor_co_nm; //금융회사명
	private String prdt_feature; //상품 특징
	private String description; //상품 설명
	private String join_way; //가입 방법
	private String mtrt_int; //만기 후 이자율
	private String spcl_cnd; //우대조건
	private String join_deny; //가입 제한 (join_deny) - 1:제한없음, 2:서민전용, 3:일부제한
	private String join_member; //가입 대상
	private String etc_note; //기타 유의사항
	private String max_limit; //최고 한도
	private Double tendency; //투자 성향

	private List<DetailOptionList> optionList; // 옵션 목록

	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class DetailOptionList { //OptionList로 이름 다시 변경해야함
		private String intr_rate_type_nm;
		private String rsrv_type_nm;
		private String save_trm;    // 저축 기간 [단위: 개월]
		private Double intr_rate;    // 금리
		private Double intr_rate2;    // 최고 우대금리
	}
}
