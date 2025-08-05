package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDepositsDto {

	/**
	 * 적금(savingsDeposits) 상품 목록 조회 페이지에서 사용될 DTO입니다.
	 */
	private String finPrdtCd;      // 상품코드
	private String finPrdtNm;      // 상품명
	private String prdtFeature;    // 상품특징
	private List<OptionList> optionList;

	/**
	 * 적금(savingsDeposits) 상품에 해당하는
	 * 여러 개의 option 값을 처리하기 위해 사용한 inner class 입니다.
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OptionList {
		private String saveTrm; // 저축 기간 [단위: 개월]
		private Double intrRate; // 금리
		private Double intrRate2; // 최고 우대금리
	}
}
