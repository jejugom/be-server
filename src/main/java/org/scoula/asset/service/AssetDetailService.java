package org.scoula.asset.service;

import org.scoula.asset.dto.AssetDetailDTO;

import java.util.List;

public interface AssetDetailService {
    List<AssetDetailDTO> getAssetDetailsByEmail(String email);
    AssetDetailDTO getAssetDetailById(Integer id);
    void addAssetDetail(AssetDetailDTO assetDetailDTO);
    void updateAssetDetail(AssetDetailDTO assetDetailDTO);
    void deleteAssetDetail(Integer id);
}
