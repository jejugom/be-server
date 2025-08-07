package org.scoula.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.domain.ProductVo;

@Mapper
public interface ProductMapper {

	/**
	 * 모든 상품종류(예금, 적금, 주담, 펀드, 금)를  전체검색하기
	 * @return
	 */
	List<? extends ProductVo> findAllProduct();

	/**
	 * 금융 상품 상세 정보 조회
	 * @param finPrdtCd 금융상품 코드
	 */
	ProductVo findProductDetail(String finPrdtCd);


















	// /**
	//  * 상품 코드로 상품 이름을 조회
	//  * @param finPrdtCd 금융 상품 코드
	//  * @return String 상품 이름
	//  */
	// String findNameByCode(@Param("finPrdtCd") String finPrdtCd);
	//
	// /**
	//  * 상품 코드로 상품 카테고리를 조회
	//  * @param finPrdtCd 금융 상품 코드
	//  * @return String 상품 카테고리
	//  */
	// String findCategoryByFinPrdtCd(String finPrdtCd);
	// List<ProductVo> getAllProduct();
}
