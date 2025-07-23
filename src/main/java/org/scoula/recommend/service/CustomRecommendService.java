package org.scoula.recommend.service;

import java.util.List;

import org.scoula.recommend.dto.CustomRecommendDto;

public interface CustomRecommendService {
	List<CustomRecommendDto> getCustomRecommendsByEmail(String email);

	void addCustomRecommend(CustomRecommendDto customRecommendDto);

	void updateCustomRecommend(CustomRecommendDto customRecommendDto);

	void deleteCustomRecommend(String email, String prdtId);
}
