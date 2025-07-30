package org.scoula.product.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.dto.GoldProductDetailDto;
import org.scoula.product.dto.GoldProductsDto;

@Mapper
public interface GoldProductsMapper {


	// 모든 금 상품의 기본 정보(fin_prdt)를 가져오는 메서드(금 상품은 옵션 테이블x)
	List<GoldProductsDto> findAllGoldProducts();

	// fin_prdt_option을 기준으로 상품 상세 조회
	GoldProductDetailDto findGoldProductByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

}
