package org.scoula.gift.dto;

import java.util.List;

import org.scoula.asset.dto.AssetStatusSummaryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 증여 페이지에 필요한 모든 데이터를 담는 최종 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftPageResponseDto {
	// 1. 수증자 목록 정보
	private List<RecipientResponseDto> recipients;

	// 2. 자산 현황 요약 정보
	private List<AssetStatusSummaryDto> assetSummary;
}
