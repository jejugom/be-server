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
public class AssetStatusResponseDto {
	private int assetId;
	private String email;
	private String assetCategoryCode;
	private Long amount;
	private String assetName;
	private String businessType;

	public static AssetStatusResponseDto of(AssetStatusVo assetStatus) {
		return AssetStatusResponseDto.builder()
			.assetId(assetStatus.getAssetId())
			.email(assetStatus.getEmail())
			.assetCategoryCode(assetStatus.getAssetCategoryCode())
			.amount(assetStatus.getAmount())
			.assetName(assetStatus.getAssetName())
			.businessType(assetStatus.getBusinessType())
			.build();
	}

	public AssetStatusVo toVo() {
		return AssetStatusVo.builder()
			.assetId(assetId)
			.email(email)
			.assetCategoryCode(assetCategoryCode)
			.amount(amount)
			.assetName(assetName)
			.businessType(businessType)
			.build();
	}
}
