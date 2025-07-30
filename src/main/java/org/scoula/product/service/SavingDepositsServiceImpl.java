package org.scoula.product.service;

import java.util.List;

import org.scoula.exception.ProductNotFoundException;
import org.scoula.product.dto.SavingsDepositsDetailDto;
import org.scoula.product.mapper.SavingsDepositsMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavingDepositsServiceImpl implements SavingDepositsService {

	private final SavingsDepositsMapper savingsDepositsMapper;

	@Override
	public SavingsDepositsDetailDto getDetail(String finPrdtCd) {
		SavingsDepositsDetailDto product = savingsDepositsMapper.findSavingsDepositByFinPrdtCd(finPrdtCd);

		if (product == null) {
			throw new ProductNotFoundException(finPrdtCd);
		}

		List<SavingsDepositsDetailDto.DetailOptionList> options = savingsDepositsMapper.findDetailOptionByFinPrdtCd(finPrdtCd);

		product.setOptionList(options);
		return product;
	}
}
