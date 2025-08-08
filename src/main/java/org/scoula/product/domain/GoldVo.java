package org.scoula.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * vo.Gold(금)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoldVo extends ProductVo<Object>{

	private String lot; //거래단위 0.01g
	private String currency; //통화단위 원화 KRW
	private String etcNote;
}
