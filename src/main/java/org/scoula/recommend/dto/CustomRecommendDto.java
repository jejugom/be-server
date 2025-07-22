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
	private String prdtId;
	private String email;
	private String recReason;
	private String segment;

	public static CustomRecommendDto of(CustomRecommendVo customRecommend) {
		return CustomRecommendDto.builder()
			.prdtId(customRecommend.getPrdtId())
			.email(customRecommend.getEmail())
			.recReason(customRecommend.getRecReason())
			.segment(customRecommend.getSegment())
			.build();
	}

	public CustomRecommendVo toVo() {
		return CustomRecommendVo.builder()
			.prdtId(prdtId)
			.email(email)
			.recReason(recReason)
			.segment(segment)
			.build();
	}
}
