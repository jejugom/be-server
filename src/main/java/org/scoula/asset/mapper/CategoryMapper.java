package org.scoula.asset.mapper;

import org.scoula.asset.domain.CategoryVO;

import java.util.List;

public interface CategoryMapper {
    List<CategoryVO> getAllCategories();
    CategoryVO getCategoryByCode(String code);
    void insertCategory(CategoryVO category);
    int updateCategory(CategoryVO category);
    int deleteCategory(String code);
}
