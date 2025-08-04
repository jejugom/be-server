package org.scoula.gift.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 최상위 응답 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResponseDto {
	private long totalEstimatedTax; // 총 예상 증여세액
	private List<RecipientTaxDetailDto> recipientDetails;
	private List<String> taxSavingStrategies;
}