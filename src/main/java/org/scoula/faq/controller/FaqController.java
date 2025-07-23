package org.scoula.faq.controller;

import java.util.List;

import org.scoula.faq.dto.FaqDto;
import org.scoula.faq.dto.FaqListDto;
import org.scoula.faq.service.FaqService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faq")
public class FaqController {

	private final FaqService faqService;

	// 1. FAQ 목록 페이지만 보여줄 때
	@GetMapping("/list")
	public ResponseEntity<List<FaqListDto>> faqListPage() {
		List<FaqListDto> faqList = faqService.getFaqList();
		return ResponseEntity.ok(faqList);
	}

	// 2. 팝업 등에서 모든 상세 정보를 미리 로딩할 때
	@GetMapping("/all")
	public ResponseEntity<List<FaqDto>> faqAllDetails() {
		List<FaqDto> faqsWithContent = faqService.getAllFaqsWithContent();
		return ResponseEntity.ok(faqsWithContent);
	}

	@GetMapping("/{faqId}")
	public ResponseEntity<FaqDto> getFaqById(@PathVariable Integer faqId) {
		return ResponseEntity.ok(faqService.getFaqById(faqId));
	}

	// @PostMapping
	// public ResponseEntity<Void> addFaq(@RequestBody FaqDto faqDto) {
	// 	faqService.addFaq(faqDto);
	// 	return ResponseEntity.ok().build();
	// }
	//
	// @PutMapping("/{faqId}")
	// public ResponseEntity<Void> updateFaq(@PathVariable Integer faqId, @RequestBody FaqDto faqDto) {
	// 	faqDto.setFaqId(faqId);
	// 	faqService.updateFaq(faqDto);
	// 	return ResponseEntity.ok().build();
	// }
	//
	// @DeleteMapping("/{faqId}")
	// public ResponseEntity<Void> deleteFaq(@PathVariable Integer faqId) {
	// 	faqService.deleteFaq(faqId);
	// 	return ResponseEntity.ok().build();
	// }
}
