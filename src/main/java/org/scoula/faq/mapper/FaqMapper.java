package org.scoula.faq.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.scoula.faq.domain.FaqVo;

public interface FaqMapper {
	List<FaqVo> getAllFaqs();

	FaqVo getFaqById(Integer id);

	// void insertFaq(FaqVo faq);
	//
	// int updateFaq(FaqVo faq);
	//
	// int deleteFaq(Integer id);
	void updateFaqPartial(@Param("faqId") Integer faqId, @Param("updates") Map<String, Object> updates);
}
