package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageLoanDetailDto {

	private String fin_prdt_cd; //상품코드
	private String fin_prdt_category; //상품 카테고리
	private String kor_co_nm; //금융회사명
	private String fin_prdt_nm; //상품명
	private String rec_reason; //추천 사유
	private String prdt_feature; //상품특징
	private String description; //상품설명
	private String join_way; //가입방법
	private String loan_inci_expn; //대출 부대비용
	private String erly_rpay_fee; //중도상환 수수료
	private String dly_rate; //연체 이자율
	private String loan_lmt; //대출한도

	private List<DetailOptionList> optionList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class DetailOptionList { //OptionList로 이름 다시 변경해야함
		private String mrtg_type_nm; //담보유형
		private String rpay_type_nm; //대출상환유형
		private String lend_rate_type_nm;    //대출금리유형
		private Double lend_rate_min;    //대출금리_최저 [소수점 2자리]
		private Double lend_rate_max;    //대출금리_최고 [소수점 2자리]
		private Double lend_rate_avg;    //전월 취급 평균금리 [소수점 2자리]
	}
}
