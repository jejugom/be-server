package org.scoula.asset.mapper;

import org.scoula.asset.domain.AssetInfoVO;

public interface AssetInfoMapper {
    AssetInfoVO getAssetInfoByEmail(String email);
    void insertAssetInfo(AssetInfoVO assetInfo);
    int updateAssetInfo(AssetInfoVO assetInfo);
    int deleteAssetInfo(String email);
}
