package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Fund(펀드) 간단 옵션 DTO
 * 펀드 상품 전체 목록 조회 시 필요한 필드만 사용하기 위함
 */
@SuperBuilder
@ToString(callSuper = true)
@Getter
public class FundSimpleOptionDto {
	private String rate3mon;       // 3개월 수익률
	private String riskGrade;      // 위험등급
	private String priceStd;       // 기준가
}
