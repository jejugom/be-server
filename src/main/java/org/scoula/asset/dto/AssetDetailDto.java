package org.scoula.asset.dto;

import java.util.Date;

import org.scoula.asset.domain.AssetDetailVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailDto {
	private int assetId;
	private String email;
	private String assetCategoryCode;
	private Long amount;
	private Date registeredAt;
	private Date endDate;
	private String assetName;
	private String businessType;
	private String categoryAssetCategoryCode;

	public static AssetDetailDto of(AssetDetailVo assetDetail) {
		return AssetDetailDto.builder()
			.assetId(assetDetail.getAssetId())
			.email(assetDetail.getEmail())
			.assetCategoryCode(assetDetail.getAssetCategoryCode())
			.amount(assetDetail.getAmount())
			.registeredAt(assetDetail.getRegisteredAt())
			.endDate(assetDetail.getEndDate())
			.assetName(assetDetail.getAssetName())
			.businessType(assetDetail.getBusinessType())
			.categoryAssetCategoryCode(assetDetail.getCategoryAssetCategoryCode())
			.build();
	}

	public AssetDetailVo toVO() {
		return AssetDetailVo.builder()
			.assetId(assetId)
			.email(email)
			.assetCategoryCode(assetCategoryCode)
			.amount(amount)
			.registeredAt(registeredAt)
			.endDate(endDate)
			.assetName(assetName)
			.businessType(businessType)
			.categoryAssetCategoryCode(categoryAssetCategoryCode)
			.build();
	}
}
