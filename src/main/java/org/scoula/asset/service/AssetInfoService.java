package org.scoula.asset.service;

import org.scoula.asset.dto.AssetInfoDTO;

public interface AssetInfoService {
    AssetInfoDTO getAssetInfoByEmail(String email);
    void addAssetInfo(AssetInfoDTO assetInfoDTO);
    void updateAssetInfo(AssetInfoDTO assetInfoDTO);
    void deleteAssetInfo(String email);
}
