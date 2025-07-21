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
    private String prdtId;
    private String email;
    private String recReason;
    private String segment;

    public static CustomRecommendDTO of(CustomRecommendVO customRecommend) {
        return CustomRecommendDTO.builder()
                .prdtId(customRecommend.getPrdtId())
                .email(customRecommend.getEmail())
                .recReason(customRecommend.getRecReason())
                .segment(customRecommend.getSegment())
                .build();
    }

    public CustomRecommendVO toVO() {
        return CustomRecommendVO.builder()
                .prdtId(prdtId)
                .email(email)
                .recReason(recReason)
                .segment(segment)
                .build();
    }
}
