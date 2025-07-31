package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageLoanDto {

	/**
	 * 주택담보대출(mortgageLoan) 상품 목록 조회 페이지에서 사용될 DTO입니다.
	 */
	private String finPrdtCd;      // 상품코드
	private String finPrdtNm;      // 상품명
	private String prdtFeature;    // 상품특징
	private List<OptionList> optionList; // 옵션 목록

	/**
	 * 주택담보대출(mortgageLoan) 상품에 해당하는
	 * 여러 개의 option 값을 처리하기 위해 사용한 inner class 입니다.
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class OptionList {
		private String mrtgTypeNm;       // 담보유형
		private String rpayTypeNm;       // 대출상환유형
		private String lendRateTypeNm;   // 대출금리유형
		private String lendRateMin;      // 대출금리_최저 [소수점 2자리]
		private String lendRateMax;      // 대출금리_최고 [소수점 2자리]
	}
}
