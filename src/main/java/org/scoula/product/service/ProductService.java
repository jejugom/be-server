package org.scoula.product.service;

import java.util.List;
import java.util.Map;

import org.scoula.product.domain.FundDailyReturnVo;
import org.scoula.product.domain.ProductVo;
import org.scoula.product.dto.ProductDto;

public interface ProductService {
	// public String getProductNameByCode(String finPrdtCd);

	/**
	 * 모든 금융 상품 조회 map 형태로 조회
	 * @return
	 */
	public Map<String, List<? extends ProductDto>> findAllProducts();

	/**
	 * 상품코드(finPrdtCd)로 해당 금융상품 상세정보 조회
	 * @param finPrdtCd
	 * @return
	 */
	public ProductVo getProductDetail(String finPrdtCd);

	String getProductNameByCode(String finPrdtCode);
	List<FundDailyReturnVo> getFundDailyReturnByCode(String finPrdtCd);
}
