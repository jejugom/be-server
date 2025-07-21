package org.scoula.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.asset.domain.CategoryVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private String assetCategoryCode;
    private String name;

    public static CategoryDTO from(CategoryVO category) {
        return CategoryDTO.builder()
                .assetCategoryCode(category.getAssetCategoryCode())
                .name(category.getName())
                .build();
    }

    public CategoryVO toVO() {
        return CategoryVO.builder()
                .assetCategoryCode(assetCategoryCode)
                .name(name)
                .build();
    }
}
