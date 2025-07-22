package org.scoula.faq.controller;

import java.util.List;

import org.scoula.faq.dto.FaqDto;
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

	@GetMapping
	public ResponseEntity<List<FaqDto>> getAllFaqs() {
		return ResponseEntity.ok(faqService.getAllFaqs());
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
