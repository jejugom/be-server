package org.scoula.asset.mapper;

import java.util.List;

import org.scoula.asset.domain.AssetDetailVo;

import org.apache.ibatis.annotations.Param;

public interface AssetDetailMapper {
	List<AssetDetailVo> getAssetDetailsByEmail(String email);

	AssetDetailVo getAssetDetailById(Integer id);

	void insertAssetDetail(AssetDetailVo assetDetail);

	int updateAssetDetail(AssetDetailVo assetDetail);

	int deleteAssetDetail(@Param("assetId") Integer assetId, @Param("userEmail") String userEmail);
}
