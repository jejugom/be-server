package org.scoula.recommend.controller;

import java.util.List;

import org.scoula.recommend.dto.CustomRecommendDto;
import org.scoula.recommend.service.CustomRecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom-recommends")
public class CustomRecommendController {

	private final CustomRecommendService customRecommendService;

	@GetMapping("/user/{email}")
	public ResponseEntity<List<CustomRecommendDto>> getCustomRecommendsByEmail(@PathVariable String email) {
		return ResponseEntity.ok(customRecommendService.getCustomRecommendsByEmail(email));
	}

	// @PostMapping
	// public ResponseEntity<Void> addCustomRecommend(@RequestBody CustomRecommendDto customRecommendDto) {
	// 	customRecommendService.addCustomRecommend(customRecommendDto);
	// 	return ResponseEntity.ok().build();
	// }

	// @PutMapping("/{email}/{prdtId}")
	// public ResponseEntity<Void> updateCustomRecommend(@PathVariable String email, @PathVariable String prdtId,
	// 	@RequestBody CustomRecommendDto customRecommendDto) {
	// 	customRecommendDto.setEmail(email);
	// 	customRecommendDto.setPrdtId(prdtId);
	// 	customRecommendService.updateCustomRecommend(customRecommendDto);
	// 	return ResponseEntity.ok().build();
	// }
	//
	// @DeleteMapping("/{email}/{prdtId}")
	// public ResponseEntity<Void> deleteCustomRecommend(@PathVariable String email, @PathVariable String prdtId) {
	// 	customRecommendService.deleteCustomRecommend(email, prdtId);
	// 	return ResponseEntity.ok().build();
	// }
}
