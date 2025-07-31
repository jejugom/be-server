package org.scoula.product.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundProductsDetailDto {

	private String finPrdtCd; // 금융 상품 코드
	private String finPrdtCategory; // 금융 상품 카테고리
	private String finPrdtNm; // 금융 상품명
	private String recReason; // 추천 사유
	private String korCoNm; // 금융회사명
	private String prdtFeature; // 상품 특징
	private String description; // 상품 설명
	private Double tendency; // 투자 성향

	private List<DetailOptionList> optionList; // 옵션 목록

	@Data
	@AllArgsConstructor
	@NoArgsConstructor // MyBatis가 객체를 생성할 때 필요
	public static class DetailOptionList { //OptionList로 이름 다시 변경해야함
		private String rate3mon;       // 3개월 수익률
		private String startDate;   // 시작일
		private String assetTotal;     // 총자산
		private String totalFee;       // 총보수
		private String riskGrade;      // 위험등급
		private String feeFirst;       // 선취수수료
		private String feeRedemp;      // 환매수수료
		private String priceStd;       // 기준가
	}
}
