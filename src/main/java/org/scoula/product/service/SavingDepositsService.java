package org.scoula.product.service;

import org.scoula.product.dto.SavingsDepositsDetailDto;

public interface SavingDepositsService {

	SavingsDepositsDetailDto getDetail(String finPrdtCd);
}
