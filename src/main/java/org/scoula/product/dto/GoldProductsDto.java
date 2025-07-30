package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoldProductsDto {

	private String fin_prdt_cd; //상품코드
	private String fin_prdt_nm; //상품명
	private String prdt_feature; //상품특징
}
