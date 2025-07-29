package org.scoula.asset.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.asset.domain.AssetStatusVo;

@Mapper
public interface AssetStatusMapper {
	List<AssetStatusVo> findAssetStatusByEmail(String email);

	AssetStatusVo findAssetStatusById(Integer id);

	void insertAssetStatus(AssetStatusVo assetStatus);

	int updateAssetStatus(AssetStatusVo assetStatus);

	int deleteAssetStatus(@Param("assetId") Integer assetId, @Param("email") String email);

	// email로 모든 자산 정보를 삭제
	int deleteByEmail(@Param("email") String email);
}
