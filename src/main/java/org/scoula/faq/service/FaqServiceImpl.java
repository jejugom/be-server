package org.scoula.faq.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.faq.dto.FaqDto;
import org.scoula.faq.mapper.FaqMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

	private final FaqMapper faqMapper;

	@Override
	public List<FaqDto> getAllFaqs() {
		return faqMapper.getAllFaqs().stream()
			.map(FaqDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public FaqDto getFaqById(Integer faqId) {
		return Optional.ofNullable(faqMapper.getFaqById(faqId))
			.map(FaqDto::of)
			.orElseThrow(() -> new NoSuchElementException("FAQ not found with id: " + faqId));
	}

	// @Override
	// public void addFaq(FaqDto faqDto) {
	// 	faqMapper.insertFaq(faqDto.toVo());
	// }
	//
	// @Override
	// public void updateFaq(FaqDto faqDto) {
	// 	if (faqMapper.updateFaq(faqDto.toVo()) == 0) {
	// 		throw new NoSuchElementException("FAQ not found with id: " + faqDto.getFaqId());
	// 	}
	// }
	//
	// @Override
	// public void deleteFaq(Integer faqId) {
	// 	if (faqMapper.deleteFaq(faqId) == 0) {
	// 		throw new NoSuchElementException("FAQ not found with id: " + faqId);
	// 	}
	// }
}
