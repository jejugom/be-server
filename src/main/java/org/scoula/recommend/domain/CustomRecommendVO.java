package org.scoula.recommend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRecommendVO {
    private String email;
    private String code;
    private String recReason;
    private String segment;
}
