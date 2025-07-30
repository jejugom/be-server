package org.scoula.asset.dto;

import org.scoula.asset.domain.AssetStatusVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 노후 메인 페이지에서 보여질 자산현황에 필요한 데이터만 담은 DTO입니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetStatusSummaryDto {
	private String assetCategoryCode;
	private Long amount;

	public static AssetStatusSummaryDto of(AssetStatusVo assetStatus) {
		return AssetStatusSummaryDto.builder()
			.assetCategoryCode(assetStatus.getAssetCategoryCode())
			.amount(assetStatus.getAmount())
			.build();
	}
}
