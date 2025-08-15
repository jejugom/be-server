package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Fund(펀드) DTO
 */
@SuperBuilder
@ToString(callSuper = true)
@Getter
public class FundDto extends ProductDetailDto<FundOptionDto> {
}
