package org.scoula.asset.controller;

import java.util.List;

import org.scoula.asset.dto.CategoryDto;
import org.scoula.asset.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@GetMapping("/{assetCategoryCode}")
	public ResponseEntity<CategoryDto> getCategoryByCode(@PathVariable String assetCategoryCode) {
		return ResponseEntity.ok(categoryService.getCategoryByCode(assetCategoryCode));
	}

	@PostMapping
	public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto) {
		categoryService.addCategory(categoryDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{assetCategoryCode}")
	public ResponseEntity<Void> updateCategory(@PathVariable String assetCategoryCode,
		@RequestBody CategoryDto categoryDto) {
		categoryDto.setAssetCategoryCode(assetCategoryCode);
		categoryService.updateCategory(categoryDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{assetCategoryCode}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String assetCategoryCode) {
		categoryService.deleteCategory(assetCategoryCode);
		return ResponseEntity.ok().build();
	}
}
