package org.scoula.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 금융 상품 옵션 정보를 담는 VO (Value Object) 클래스
 * fin_prdt_option 테이블의 레코드와 매핑됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionVo {

	/** 옵션 코드 (option_code) */
	private int optionCode;

	/** 금융 상품 코드 (fin_prdt_cd) */
	private String finPrdtCd;

	/** 저축 금리 유형명 (intr_rate_type_nm) */
	private String intrRateTypeNm;

	/** 저축 기간 (save_trm) */
	private String saveTrm;

	/** 저축 금리 (intr_rate) */
	private Double intrRate;

	/** 최고 우대금리 (intr_rate2) */
	private Double intrRate2;

	/** 적립 유형명 (rsrv_type_nm) */
	private String rsrvTypeNm;

	/** 주택담보대출 금리유형 (mrtg_type_nm) */
	private String mrtgTypeNm;

	/** 상환 방식 (rpay_type_nm) */
	private String rpayTypeNm;

	/** 대출금리유형 (lend_rate_type_nm) */
	private String lendRateTypeNm;

	/** 대출 최저금리 (lend_rate_min) */
	private Double lendRateMin;

	/** 대출 최고금리 (lend_rate_max) */
	private Double lendRateMax;

	/** 전월 취급 평균금리 (lend_rate_avg) */
	private Double lendRateAvg;
}
