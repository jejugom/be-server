package org.scoula.recommend.mapper;

import java.util.List;

import org.scoula.recommend.domain.CustomRecommendVo;

public interface CustomRecommendMapper {
	List<CustomRecommendVo> getCustomRecommendsByEmail(String email);

	void insertCustomRecommend(CustomRecommendVo customRecommend);

	int updateCustomRecommend(CustomRecommendVo customRecommend);

	int deleteCustomRecommend(String email, String code);
}
