package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundProductsDto {

	private String finPrdtCd; // 상품코드
	private String finPrdtNm; // 상품명
	private String prdtFeature; // 상품특징

	private List<OptionList> optionList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OptionList {
		private String rate3mon;       // 3개월 수익률
		private String riskGrade;      // 위험등급
		private String priceStd;       // 기준가
	}
}
