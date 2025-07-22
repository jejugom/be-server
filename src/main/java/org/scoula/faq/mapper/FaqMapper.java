package org.scoula.faq.mapper;

import java.util.List;

import org.scoula.faq.domain.FaqVo;

public interface FaqMapper {
	List<FaqVo> getAllFaqs();

	FaqVo getFaqById(Integer id);

	void insertFaq(FaqVo faq);

	int updateFaq(FaqVo faq);

	int deleteFaq(Integer id);
}
