package org.scoula.asset.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.asset.dto.CategoryDto;
import org.scoula.asset.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public List<CategoryDto> getAllCategories() {
		return categoryMapper.getAllCategories().stream()
			.map(CategoryDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public CategoryDto getCategoryByCode(String assetCategoryCode) {
		return Optional.ofNullable(categoryMapper.getCategoryByCode(assetCategoryCode))
			.map(CategoryDto::from)
			.orElseThrow(() -> new NoSuchElementException("Category not found with code: " + assetCategoryCode));
	}

	@Override
	public void addCategory(CategoryDto categoryDto) {
		categoryMapper.insertCategory(categoryDto.toVO());
	}

	@Override
	public void updateCategory(CategoryDto categoryDto) {
		if (categoryMapper.updateCategory(categoryDto.toVO()) == 0) {
			throw new NoSuchElementException("Category not found with code: " + categoryDto.getAssetCategoryCode());
		}
	}

	@Override
	public void deleteCategory(String assetCategoryCode) {
		if (categoryMapper.deleteCategory(assetCategoryCode) == 0) {
			throw new NoSuchElementException("Category not found with code: " + assetCategoryCode);
		}
	}
}
