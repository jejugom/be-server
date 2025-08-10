package org.scoula.gpt.dto;

import java.util.List;

import lombok.Data;

@Data
public class DataResponseDto {
	private String concept_name;
	private String description;
	private List<TermDto> terms;
}