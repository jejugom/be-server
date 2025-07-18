package org.scoula.asset.service;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.domain.CategoryVO;
import org.scoula.asset.dto.CategoryDTO;
import org.scoula.asset.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.getAllCategories().stream()
                .map(CategoryDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryByCode(String code) {
        return Optional.ofNullable(categoryMapper.getCategoryByCode(code))
                .map(CategoryDTO::of)
                .orElseThrow(() -> new NoSuchElementException("Category not found with code: " + code));
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        categoryMapper.insertCategory(categoryDTO.toVO());
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        if (categoryMapper.updateCategory(categoryDTO.toVO()) == 0) {
            throw new NoSuchElementException("Category not found with code: " + categoryDTO.getCode());
        }
    }

    @Override
    public void deleteCategory(String code) {
        if (categoryMapper.deleteCategory(code) == 0) {
            throw new NoSuchElementException("Category not found with code: " + code);
        }
    }
}
