package org.scoula.asset.mapper;

import org.scoula.asset.domain.AssetInfoVo;

public interface AssetInfoMapper {
	AssetInfoVo getAssetInfoByEmail(String email);

	void insertAssetInfo(AssetInfoVo assetInfo);

	int updateAssetInfo(AssetInfoVo assetInfo);

	int deleteAssetInfo(String email);
}
