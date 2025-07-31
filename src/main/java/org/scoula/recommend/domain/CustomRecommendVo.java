package org.scoula.recommend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRecommendVo {
	private String userEmail; //사용자 이메일
	private String finPrdtCd; //상품코드
	private String score; //적합률
}
