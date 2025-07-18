package org.scoula.asset.mapper;

import org.scoula.asset.domain.AssetDetailVO;

import java.util.List;

public interface AssetDetailMapper {
    List<AssetDetailVO> getAssetDetailsByEmail(String email);
    AssetDetailVO getAssetDetailById(Integer id);
    void insertAssetDetail(AssetDetailVO assetDetail);
    int updateAssetDetail(AssetDetailVO assetDetail);
    int deleteAssetDetail(Integer id);
}
