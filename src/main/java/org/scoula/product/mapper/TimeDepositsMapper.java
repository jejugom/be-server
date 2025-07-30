package org.scoula.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.dto.TimeDepositsDetailDto;
import org.scoula.product.dto.TimeDepositsDetailDto.DetailOptionList;
import org.scoula.product.dto.TimeDepositsDto;
import org.scoula.product.dto.TimeDepositsDto.OptionList;

@Mapper
public interface TimeDepositsMapper {

	// 모든 예금 상품의 기본 정보 (fin_prdt 테이블)를 가져오는 메서드
	List<TimeDepositsDto> findAllTimeDeposits();

	// 특정 예금 상품 코드에 해당하는 옵션 정보 (fin_prdt_option 테이블)를 가져오는 메서드
	List<OptionList> findOptionsByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	// fin_prdt_option을 기준으로 상품 상세 조회
	TimeDepositsDetailDto findTimeDepositByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);

	List<TimeDepositsDetailDto.DetailOptionList> findDetailOptionByFinPrdtCd(@Param("finPrdtCd") String finPrdtCd);
}
