package org.scoula.product.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper {

	/**
	 * 상품 코드로 상품 이름을 조회
	 * @param finPrdtCd 금융 상품 코드
	 * @return String 상품 이름
	 */
	String findNameByCode(@Param("finPrdtCd") String finPrdtCd);
}
