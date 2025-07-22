package org.scoula.asset.dto;

import org.scoula.asset.domain.CategoryVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
	private String assetCategoryCode;
	private String name;

	public static CategoryDto from(CategoryVo category) {
		return CategoryDto.builder()
			.assetCategoryCode(category.getAssetCategoryCode())
			.name(category.getName())
			.build();
	}

	public CategoryVo toVo() {
		return CategoryVo.builder()
			.assetCategoryCode(assetCategoryCode)
			.name(name)
			.build();
	}
}
