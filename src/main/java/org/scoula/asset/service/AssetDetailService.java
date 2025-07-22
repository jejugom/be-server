package org.scoula.asset.service;

import java.util.List;

import org.scoula.asset.dto.AssetDetailDto;

public interface AssetDetailService {
	List<AssetDetailDto> getAssetDetailsByEmail(String email);

	AssetDetailDto getAssetDetailById(Integer assetId);

	void addAssetDetail(AssetDetailDto assetDetailDto);

	void updateAssetDetail(AssetDetailDto assetDetailDto);

	void deleteAssetDetail(Integer assetId);
}
