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
    private String prdtId;
    private String email;
    private String recReason;
    private String segment;
}
