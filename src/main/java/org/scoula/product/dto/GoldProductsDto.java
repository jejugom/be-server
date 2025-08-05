package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoldProductsDto {

	private String finPrdtCd; // 상품코드
	private String finPrdtNm; // 상품명
	private String prdtFeature; // 상품특징
}
