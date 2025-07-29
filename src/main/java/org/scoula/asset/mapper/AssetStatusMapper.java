package org.scoula.asset.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.scoula.asset.domain.AssetStatusVo;

@Mapper
public interface AssetStatusMapper {
	List<AssetStatusVo> findAssetStatusByEmail(String email);

	AssetStatusVo findAssetStatusById(Integer id);

	void insertAssetStatus(AssetStatusVo assetStatus);

	int updateAssetStatus(AssetStatusVo assetStatus);

	int deleteAssetStatus(@Param("assetId") Integer assetId, @Param("userEmail") String userEmail);

	int deleteByEmail(@Param("email") String email);
}
