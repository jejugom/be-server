package org.scoula.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductClickStatsDto {

	private String finPrdtCd;
	private Long clickCount;
}
