package org.scoula.faq.service;

import java.util.List;

import org.scoula.faq.dto.FaqDto;
import org.scoula.faq.dto.FaqListDto;

public interface FaqService {
	List<FaqListDto> getFaqList();

	List<FaqDto> getAllFaqsWithContent();

	FaqDto getFaqById(Integer faqId);

	// void addFaq(FaqDto faqDto);
	//
	// void updateFaq(FaqDto faqDto);
	//
	// void deleteFaq(Integer faqId);
}
