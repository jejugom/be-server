package org.scoula.asset.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.asset.domain.CategoryVo;

@Mapper
public interface CategoryMapper {
	List<CategoryVo> getAllCategories();

	CategoryVo getCategoryByCode(String code);

	void insertCategory(CategoryVo category);

	int updateCategory(CategoryVo category);

	int deleteCategory(String code);
}
