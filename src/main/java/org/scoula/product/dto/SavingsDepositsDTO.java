package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDepositsDTO {

	/**
	 * 적금(savingsDeposits) 상품 목록 조회 페이지에서 사용될 DTO입니다.
	 */
	private String fin_prdt_cd; //상품코드
	private String fin_prdt_nm; //상품명
	private String prdt_feature; //상품특징
	private List<OptionList> optionList;

	/**
	 * 적금(savingsDeposits) 상품에 해당하는
	 * 여러 개의 option 값을 처리하기 위해 사용한 inner class 입니다.
	 */
	@Data
	@AllArgsConstructor
	public static class OptionList {
		private String save_trm;
		private Double intr_rate;
		private Double intr_rate2;
	}
}
