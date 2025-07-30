package org.scoula.product.service;

import org.scoula.product.dto.GoldProductDetailDto;

public interface GoldProductService {
	GoldProductDetailDto getDetail(String finPrdtCd);
}
