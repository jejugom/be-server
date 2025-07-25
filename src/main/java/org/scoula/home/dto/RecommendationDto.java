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
	private String prodId;
	private String prodName;
	private String description;
	private Double rate;
}
