package org.scoula.booking.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservedSlotsResponseDto {
	@JsonProperty("reserved_slots")
	private Map<String, List<String>> reservedSlots;
}
