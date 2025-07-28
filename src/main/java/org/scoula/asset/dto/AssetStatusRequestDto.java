package org.scoula.asset.dto;

import org.scoula.asset.domain.AssetStatusVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetStatusRequestDto {
	private String assetCategoryCode;
	private Long amount;
	private String assetName;
	private String businessType;

	public AssetStatusVo toVo() {
		return AssetStatusVo.builder()
			.assetCategoryCode(this.assetCategoryCode)
			.amount(this.amount)
			.assetName(this.assetName)
			.businessType(this.businessType)
			.build();
	}
}
