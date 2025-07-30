package org.scoula.product.service;

import org.scoula.product.dto.TimeDepositsDetailDto;

public interface TimeDepositsService {

	TimeDepositsDetailDto getDetail(String finPrdtCd);
}
