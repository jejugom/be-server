package org.scoula.product.service;

import java.util.List;
import java.util.Map;

import org.scoula.product.domain.FundDailyReturnVo;
import org.scoula.product.domain.ProductVo;
import org.scoula.product.dto.ProductDto;

public interface ProductService {
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

	/**
	 * 상품코드(finPrdtCd)로 코드와 일치하는 금융 상품명 조회
	 * @param finPrdtCd
	 * @return
	 */
	String getProductNameByCode(String finPrdtCd);

	List<FundDailyReturnVo> getFundDailyReturnByCode(String finPrdtCd);
}
