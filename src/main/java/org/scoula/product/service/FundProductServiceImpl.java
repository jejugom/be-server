package org.scoula.product.service;

import java.util.List;

import org.scoula.exception.ProductNotFoundException;
import org.scoula.product.dto.FundProductsDetailDto;
import org.scoula.product.mapper.FundProductsMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundProductServiceImpl implements FundProductService {

	private final FundProductsMapper fundProductsMapper;

	@Override
	public FundProductsDetailDto getDetail(String finPrdtCd) {

		FundProductsDetailDto product = fundProductsMapper.findFundProductsByFinPrdtCd(finPrdtCd);

		if (product == null) {
			throw new ProductNotFoundException(finPrdtCd);
		}

		List<FundProductsDetailDto.DetailOptionList> options = fundProductsMapper.findDetailOptionByFinPrdtCd(finPrdtCd);

		product.setOptionList(options);

		return product;
	}
}
