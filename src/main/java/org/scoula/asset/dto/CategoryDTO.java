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
    private String code;
    private String name;

    public static CategoryDTO of(CategoryVO category) {
        return CategoryDTO.builder()
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

    public CategoryVO toVO() {
        return CategoryVO.builder()
                .code(code)
                .name(name)
                .build();
    }
}
