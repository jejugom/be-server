package org.scoula.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.dto.SavingsDepositsDetailDto;
import org.scoula.product.dto.SavingsDepositsDto;
import org.scoula.product.dto.SavingsDepositsDto.OptionList;
import org.scoula.product.dto.TimeDepositsDetailDto;

@Mapper
public interface SavingsDepositsMapper {

	/**
	 * 모든 적금 상품의 기본 정보 (fin_prdt 테이블)를 가져오는 메서드
	 * @return 적금 상품 기본 정보 리스트
	 */
	List<SavingsDepositsDto> findAllSavingsDeposits();

	/**
	 * 특정 적금 상품 코드에 해당하는 옵션 정보 (fin_prdt_option 테이블)를 가져오는 메서드
	 * @param finPrdtCd 금융상품 코드
	 * @return 적금 상품 옵션 정보 리스트
	 */
	List<OptionList> findOptionsByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	// fin_prdt_option을 기준으로 상품 상세 조회
	SavingsDepositsDetailDto findSavingsDepositByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	List<SavingsDepositsDetailDto.DetailOptionList> findDetailOptionByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

}
