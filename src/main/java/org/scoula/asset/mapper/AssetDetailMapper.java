package org.scoula.asset.mapper;

import java.util.List;

import org.scoula.asset.domain.AssetDetailVo;

public interface AssetDetailMapper {
	List<AssetDetailVo> getAssetDetailsByEmail(String email);

	AssetDetailVo getAssetDetailById(Integer id);

	void insertAssetDetail(AssetDetailVo assetDetail);

	int updateAssetDetail(AssetDetailVo assetDetail);

	int deleteAssetDetail(Integer id);
}
