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
	private String fin_prdt_cd;
	private String rate; //적합률

	public static CustomRecommendDto of(CustomRecommendVo customRecommend) {
		return CustomRecommendDto.builder()
			.fin_prdt_cd(customRecommend.getFin_prdt_cd())
			.rate(customRecommend.getRate())
			.build();
	}

	public CustomRecommendVo toVo() {
		return CustomRecommendVo.builder()
			.fin_prdt_cd(fin_prdt_cd)
			.rate(rate)
			.build();
	}
}
