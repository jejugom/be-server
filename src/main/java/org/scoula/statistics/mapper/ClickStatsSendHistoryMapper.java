package org.scoula.statistics.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

@Mapper
public interface ClickStatsSendHistoryMapper {

	@Select("SELECT sent_at FROM click_stats_send_history ORDER BY sent_at DESC LIMIT 1")
	LocalDateTime findLastSentAt();

	@Insert("INSERT INTO click_stats_send_history(sent_at) VALUES(#{sentAt})")
	void insertSentAt(@Param("sentAt") LocalDateTime sentAt);
}
