package org.scoula.gift.dto;

import java.util.List;

import lombok.Data;

@Data
public class SimulationRequestDto {
	private List<RecipientGiftRequestDto> simulationList;
}