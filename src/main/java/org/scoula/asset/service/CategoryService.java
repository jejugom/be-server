package org.scoula.asset.service;

import java.util.List;

import org.scoula.asset.dto.CategoryDto;

public interface CategoryService {
	List<CategoryDto> getAllCategories();

	CategoryDto getCategoryByCode(String assetCategoryCode);

	void addCategory(CategoryDto categoryDto);

	void updateCategory(CategoryDto categoryDto);

	void deleteCategory(String assetCategoryCode);
}
