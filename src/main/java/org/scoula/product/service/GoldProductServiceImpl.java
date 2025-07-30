package org.scoula.product.service;

import org.scoula.exception.ProductNotFoundException;
import org.scoula.product.dto.GoldProductDetailDto;
import org.scoula.product.mapper.GoldProductsMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoldProductServiceImpl implements GoldProductService {

	private final GoldProductsMapper goldProductsMapper;

	@Override
	public GoldProductDetailDto getDetail(String finPrdtCd) {
		GoldProductDetailDto product = goldProductsMapper.findGoldProductByFinPrdtCd(finPrdtCd);

		if (product == null) {
			throw new ProductNotFoundException(finPrdtCd);
		}

		return product;
	}
}
