package org.scoula.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.dto.FundProductsDetailDto;
import org.scoula.product.dto.FundProductsDto;

@Mapper
public interface FundProductsMapper {

	// 모든 펀드 상품의 기본 정보 (fin_prdt 테이블)를 가져오는 메서드
	List<FundProductsDto> findAllFundProducts();

	// 특정 펀드 상품 코드에 해당하는 옵션 정보 (fund_prdt_option 테이블)를 가져오는 메서드
	List<FundProductsDto.OptionList> findOptionsByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	// fund_prdt_option 기준으로 상품 상세 조회
	FundProductsDetailDto findFundProductsByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	List<FundProductsDetailDto.DetailOptionList> findDetailOptionByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

}
