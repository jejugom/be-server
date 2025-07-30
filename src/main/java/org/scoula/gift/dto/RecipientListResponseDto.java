package org.scoula.gift.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientListResponseDto {
	private List<RecipientResponseDto> recipients;
	private int totalCount; // (예시) 목록의 전체 개수
}
