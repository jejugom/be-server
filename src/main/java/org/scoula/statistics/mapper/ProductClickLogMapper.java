package org.scoula.statistics.mapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.statistics.domain.ProductClickLogVo;
import org.scoula.statistics.dto.ProductClickStatsDto;

/**
 * 상품 예약하기 클릭 데이터 저장 Mapper
 */
@Mapper
public interface ProductClickLogMapper {

	void insertClickLog(ProductClickLogVo log);

	/**
	 * 특정 기간 이후의 클릭 수 집계
	 * @param fromDate
	 * @return
	 */
	List<ProductClickStatsDto> selectClickStatsSince(@Param("fromDate") LocalDateTime fromDate);

	void deleteClickLogsBefore(@Param("toDate") LocalDateTime toDate);
}
