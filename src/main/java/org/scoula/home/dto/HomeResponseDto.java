package org.scoula.home.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeResponseDto {
	private UserSummary userSummary; // 로그인 사용자만 존재
	private List<RecommendationDto> recommandTop3;
}
