package org.scoula.product.service;

import org.scoula.product.dto.MortgageLoanDetailDto;

public interface MortgageLoanService {

	MortgageLoanDetailDto getDetail(String finPrdtCd);
}
