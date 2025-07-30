package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageLoanDetailDto {

	private String finPrdtCd; //상품코드
	private String finPrdtCategory; //상품 카테고리
	private String korCoNm; //금융회사명
	private String finPrdtNm; //상품명
	private String recReason; //추천 사유
	private String prdtFeature; //상품특징
	private String description; //상품설명
	private String joinWay; //가입방법
	private String loanInciExpn; //대출 부대비용
	private String erlyRpayFee; //중도상환 수수료
	private String dlyRate; //연체 이자율
	private String loanLmt; //대출한도

	private List<DetailOptionList> optionList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class DetailOptionList { //OptionList로 이름 다시 변경해야함
		private String mrtgTypeNm; //담보유형
		private String rpayTypeNm; //대출상환유형
		private String lendRateTypeNm;    //대출금리유형
		private Double lendRateMin;    //대출금리_최저 [소수점 2자리]
		private Double lend_RateMax;    //대출금리_최고 [소수점 2자리]
		private Double lendRateAvg;    //전월 취급 평균금리 [소수점 2자리]
	}
}
