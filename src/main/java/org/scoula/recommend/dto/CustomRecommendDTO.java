package org.scoula.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.recommend.domain.CustomRecommendVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRecommendDTO {
    private String email;
    private String code;
    private String recReason;
    private String segment;

    public static CustomRecommendDTO of(CustomRecommendVO customRecommend) {
        return CustomRecommendDTO.builder()
                .email(customRecommend.getEmail())
                .code(customRecommend.getCode())
                .recReason(customRecommend.getRecReason())
                .segment(customRecommend.getSegment())
                .build();
    }

    public CustomRecommendVO toVO() {
        return CustomRecommendVO.builder()
                .email(email)
                .code(code)
                .recReason(recReason)
                .segment(segment)
                .build();
    }
}
