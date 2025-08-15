package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 중간 부모 클래스(상품 상세조회 시 사용되는 DTO)
 * @param <T>
 */
@SuperBuilder
@ToString
@Getter
public class ProductDetailDto<T> extends ProductDto<T> {

	private String korCoNm; //금융회사명
	private String finPrdtCategory; //상품 카테고리
	private String description; //상품설명
	private String joinWay; //가입경로
	private String recReason; //추천 사유
}
