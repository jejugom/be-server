package org.scoula.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.recommend.domain.CustomRecommendVo;

@Mapper
public interface CustomRecommendMapper {

	/**
	 * 
	 * @param email
	 * @return 사용자별 맞춤상품 리스트 반환
	 */
	List<CustomRecommendVo> getCustomRecommendsByEmail(String email);

	/**
	 * 사용자별 맞춤 상품 tbl 삽입
	 * @param customRecommend (상품코드 fin_prdt_cd, 적합률 score)
	 */
	void insertCustomRecommend(CustomRecommendVo customRecommend);

	/**
	 * 사용자별 맞춤 상품 tbl 수정
	 * @param customRecommend (상품코드 fin_prdt_cd, 적합률 score)
	 */
	int updateCustomRecommend(CustomRecommendVo customRecommend);

	/**
	 * 
	 * 사용자 이메일과 해당 상품 코드로 맞춤 상품 삭제
	 * @param email
	 * @param code
	 * @return void
	 */
	int deleteCustomRecommend(String email, String code);

	int deleteAllProductsByEmail(String email);
}
