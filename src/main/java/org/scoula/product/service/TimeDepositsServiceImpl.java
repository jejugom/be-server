package org.scoula.product.service;

import java.util.List;

import org.scoula.exception.ProductNotFoundException;
import org.scoula.product.dto.TimeDepositsDetailDto;
import org.scoula.product.mapper.TimeDepositsMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeDepositsServiceImpl implements TimeDepositsService {

	private final TimeDepositsMapper timeDepositsMapper;

	@Override
	public TimeDepositsDetailDto getDetail(String finPrdtCd) {
		TimeDepositsDetailDto product = timeDepositsMapper.findTimeDepositByFinPrdtCd(finPrdtCd);

		if (product == null) {
			throw new ProductNotFoundException(finPrdtCd);
		}

		List<TimeDepositsDetailDto.DetailOptionList> options = timeDepositsMapper.findDetailOptionByFinPrdtCd(finPrdtCd);

		product.setOptionList(options);
		return product;
	}
}
