package org.scoula.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationDto {
	private String fin_prdt_cd;
	private String fin_prdt_nm;
	private String prdt_feature;
	private Double intr_rate;
	private Double intr_rate2;
}
