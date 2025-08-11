package org.scoula.statistics.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

@Mapper
public interface StatsSendHistoryMapper {

	LocalDateTime findLastSentAt(@Param("statType") String statType);

	void insertSentAt(@Param("statType") String statType, @Param("sentAt") LocalDateTime sentAt);
}
