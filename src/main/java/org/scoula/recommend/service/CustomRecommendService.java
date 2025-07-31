package org.scoula.recommend.service;

import java.util.List;

import org.scoula.recommend.dto.CustomRecommendDto;

public interface CustomRecommendService {

	/**
	 * 사용자 이메일로 맞춤상품 목록 가져오기
	 * @param email
	 * @return
	 */
	List<CustomRecommendDto> getCustomRecommendsByEmail(String email);

	void addCustomRecommend(String email);
}
