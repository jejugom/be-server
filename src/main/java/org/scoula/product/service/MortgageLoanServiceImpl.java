package org.scoula.product.service;

import java.util.List;

import org.scoula.exception.ProductNotFoundException;
import org.scoula.product.dto.MortgageLoanDetailDto;
import org.scoula.product.mapper.MortgageLoanMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MortgageLoanServiceImpl implements MortgageLoanService {

	private final MortgageLoanMapper mortgageLoanMapper;

	@Override
	public MortgageLoanDetailDto getDetail(String finPrdtCd) {
		MortgageLoanDetailDto product = mortgageLoanMapper.findMortgageLoanByFinPrdtCd(finPrdtCd);

		if (product == null) {
			throw new ProductNotFoundException(finPrdtCd);
		}

		List<MortgageLoanDetailDto.DetailOptionList> options = mortgageLoanMapper.findDetailOptionByFinPrdtCd(
			finPrdtCd);

		product.setOptionList(options);
		return product;
	}
}
