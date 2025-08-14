package org.scoula.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 최상위 부모 DTO 클래스(상품 목록 조회시 전달하는 필드와 동일)
 * @param <T>
 */
@SuperBuilder
@ToString
@Getter
public class ProductDto<T> {

	private String finPrdtCd; //상품코드
	private String finPrdtNm; //상품명
	private String prdtFeature; //상품특성

	private List<T> optionList;
}
