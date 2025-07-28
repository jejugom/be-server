package org.scoula.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailRequestDto {
	private String assetCategoryCode;
	private Long amount;
	private String assetName;
	private String businessType;
}
