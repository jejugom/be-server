package org.scoula.product.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 상품들의 부모 클래스입니다.
 * @param <T>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode에서 객체 식별에 특정 멤버변수 값만 사용하기 위해 of속성을 사용
@EqualsAndHashCode(of = {"finPrdtCd"})
public class ProductVo<T> {
	private String finCoNo; //금융회사 코드
	private String korCoNm; //금융회사명
	private String finPrdtCategory; //상품 카테고리
	private String finPrdtCd; //상품코드
	private String finPrdtNm; //상품명
	private String prdtFeature; //상품특성
	private String description; //상품설명
	private String joinWay; //가입경로
	private String recReason; //추천 사유
	private Double tendency; //투자성향
	private Double assetProportion; //자산 구성 비율

	private List<T> optionList;
}
