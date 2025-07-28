package org.scoula.asset.service;

import java.util.List;

import org.scoula.asset.dto.AssetDetailResponseDto;

public interface AssetDetailService {
	List<AssetDetailResponseDto> getAssetDetailsByEmail(String email);

	AssetDetailResponseDto getAssetDetailById(Integer assetId);

	void addAssetDetail(AssetDetailResponseDto assetDetailResponseDto);

	void updateAssetDetail(AssetDetailResponseDto assetDetailResponseDto);

	void deleteAssetDetail(Integer assetId, String userEmail);

}
