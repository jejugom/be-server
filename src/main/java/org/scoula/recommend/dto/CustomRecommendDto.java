package org.scoula.recommend.dto;

import org.scoula.recommend.domain.CustomRecommendVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRecommendDto {
	private String finPrdtCd;
	private String score; //적합률

	public static CustomRecommendDto of(CustomRecommendVo customRecommend) {
		return CustomRecommendDto.builder()
			.finPrdtCd(customRecommend.getFinPrdtCd())
			.score(customRecommend.getScore())
			.build();
	}

	public CustomRecommendVo toVo() {
		return CustomRecommendVo.builder()
			.finPrdtCd(finPrdtCd)
			.score(score)
			.build();
	}
}
