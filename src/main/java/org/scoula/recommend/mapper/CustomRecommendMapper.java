package org.scoula.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.recommend.domain.CustomRecommendVo;

@Mapper
public interface CustomRecommendMapper {
	List<CustomRecommendVo> getCustomRecommendsByEmail(String email);

	void insertCustomRecommend(CustomRecommendVo customRecommend);

	int updateCustomRecommend(CustomRecommendVo customRecommend);

	int deleteCustomRecommend(String email, String code);
}
