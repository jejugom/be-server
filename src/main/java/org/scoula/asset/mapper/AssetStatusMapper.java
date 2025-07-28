package org.scoula.asset.mapper;

import java.util.List;

import org.scoula.asset.domain.AssetStatusVo;

import org.apache.ibatis.annotations.Param;

public interface AssetStatusMapper {
	List<AssetStatusVo> findAssetStatusByEmail(String email);

	AssetStatusVo findAssetStatusById(Integer id);

	void insertAssetStatus(AssetStatusVo assetStatus);

	int updateAssetStatus(AssetStatusVo assetStatus);

	int deleteAssetStatus(@Param("assetId") Integer assetId, @Param("userEmail") String userEmail);
}
