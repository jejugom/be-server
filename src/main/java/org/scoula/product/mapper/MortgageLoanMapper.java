package org.scoula.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.dto.MortgageLoanDetailDto;
import org.scoula.product.dto.MortgageLoanDto;
import org.scoula.product.dto.MortgageLoanDto.OptionList;

@Mapper
public interface MortgageLoanMapper {

	/**
	 * 모든 주택담보대출 상품의 기본 정보 (fin_prdt 테이블)를 가져오는 메서드
	 * @return 주택담보대출 상품 기본 정보 리스트
	 */
	List<MortgageLoanDto> findAllMortgageLoans();

	/**
	 * 특정 주택담보대출 상품 코드에 해당하는 옵션 정보 (fin_prdt_option 테이블)를 가져오는 메서드
	 * @param finPrdtCd 금융상품 코드
	 * @return 주택담보대출 상품 옵션 정보 리스트
	 */
	List<OptionList> findOptionsByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	// fin_prdt_option을 기준으로 상품 상세 조회
	MortgageLoanDetailDto findMortgageLoanByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	List<MortgageLoanDetailDto.DetailOptionList> findDetailOptionByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);


}
