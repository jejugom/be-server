package org.scoula.product.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 금융 상품 옵션 정보를 담는 VO (Value Object) 클래스
 * fund_prdt_option 테이블의 레코드와 매핑됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundProductOptionVo {

	private String finPrdtCd;      // 상품 코드
	private String rate3mon;       // 3개월 수익률
	private LocalDate startDate;   // 시작일
	private String assetTotal;     // 총자산
	private String totalFee;       // 총보수
	private String riskGrade;      // 위험등급
	private String feeFirst;       // 선취수수료
	private String feeRedemp;      // 환매수수료
	private String priceStd;       // 기준가
}
