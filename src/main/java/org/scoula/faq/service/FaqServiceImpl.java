package org.scoula.faq.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.faq.dto.FaqDto;
import org.scoula.faq.dto.FaqListDto;
import org.scoula.faq.mapper.FaqMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

	private final FaqMapper faqMapper;

	// FAQ 목록 조회를 2가지 방법으로 명세

	/**
	 * 방법 1: 목록 조회용 (id, title 등 요약 정보만 반환)
	 */
	@Override
	public List<FaqListDto> getFaqList() {
		return faqMapper.getAllFaqs().stream()
			.map(FaqListDto::from)
			.collect(Collectors.toList());
	}

	/**
	 * 방법 2: 상세 내용 포함 전체 조회용 (팝업, 페이지 이동 없는 상세보기에 사용)
	 */
	@Override
	public List<FaqDto> getAllFaqsWithContent() {
		return faqMapper.getAllFaqs().stream()
			.map(FaqDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public FaqDto getFaqById(Integer faqId) {
		return Optional.ofNullable(faqMapper.getFaqById(faqId))
			.map(FaqDto::from)
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

	@Override
	public void updateFaqPartial(Integer faqId, Map<String, Object> updates) {
		faqMapper.updateFaqPartial(faqId, updates);
	}
}
