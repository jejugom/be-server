package org.scoula.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 금융 상품 정보를 담는 VO (Value Object) 클래스
 * fin_prdt 테이블의 레코드와 매핑됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVo {

	/** 금융 상품 코드 (fin_prdt_cd) */
	private String finPrdtCd;

	/** 금융 상품 카테고리 (fin_prdt_category) */
	private String finPrdtCategory;

	/** 금융 상품명 (fin_prdt_nm) */
	private String finPrdtNm;

	/** 고객 세그먼트 (segment) */
	private String segment;

	/** 추천 사유 (rec_reason) */
	private String recReason;

	/** 금융회사명 (kor_co_nm) */
	private String korCoNm;

	/** 금융회사 코드 (fin_co_no) */
	private String finCoNo;

	/** 상품 특징 (prdt_feature) */
	private String prdtFeature;

	/** 상품 설명 (description) */
	private String description;

	/** 가입 방법 (join_way) */
	private String joinWay;

	/** 만기 후 이자율 (mtrt_int) */
	private String mtrtInt;

	/** 우대 조건 (spcl_cnd) */
	private String spclCnd;

	/** 가입 제한 (join_deny) - 1:제한없음, 2:서민전용, 3:일부제한 */
	private String joinDeny;

	/** 가입 대상 (join_member) */
	private String joinMember;

	/** 기타 유의사항 (etc_note) */
	private String etcNote;

	/** 최고 한도 (max_limit) */
	private String maxLimit;

	/** 대출 부대비용 (loan_inci_expn) */
	private String loanInciExpn;

	/** 중도상환 수수료 (erly_rpay_fee) */
	private String erlyRpayFee;

	/** 연체 이자율 (dly_rate) */
	private String dlyRate;

	/** 대출 한도 (loan_lmt) */
	private String loanLmt;

	/** 투자 성향 (tendency) */
	private Double tendency;

	/** 자산 비중 (asset_proportion) */
	private Double assetProportion;

	/** 거래 단위 (lot) */
	private String lot;

	/** 거래 통화 (currency) */
	private String currency;
}
