package org.scoula.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.product.domain.FundDailyReturnVo;

@Mapper
public interface FundDailyReturnMapper {
	List<FundDailyReturnVo> findByFundCode(@Param("fundCode") String fundCode);

}
