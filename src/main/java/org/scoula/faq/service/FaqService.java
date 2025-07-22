package org.scoula.faq.service;

import java.util.List;

import org.scoula.faq.dto.FaqDto;

public interface FaqService {
	List<FaqDto> getAllFaqs();

	FaqDto getFaqById(Integer faqId);

	// void addFaq(FaqDto faqDto);
	//
	// void updateFaq(FaqDto faqDto);
	//
	// void deleteFaq(Integer faqId);
}
