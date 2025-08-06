package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
public class GoldDto extends ProductDetailDto<Object> {

	private String lot;
	private String currency;
	private String etcNote;
}
