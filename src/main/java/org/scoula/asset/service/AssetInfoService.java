package org.scoula.asset.service;

import org.scoula.asset.dto.AssetInfoDto;

public interface AssetInfoService {
	AssetInfoDto getAssetInfoByEmail(String email);

	void addAssetInfo(AssetInfoDto assetInfoDto);

	void updateAssetInfo(AssetInfoDto assetInfoDto);

	void deleteAssetInfo(String email);
}
