package org.scoula.recommend.mapper;

import org.scoula.recommend.domain.CustomRecommendVO;

import java.util.List;

public interface CustomRecommendMapper {
    List<CustomRecommendVO> getCustomRecommendsByEmail(String email);
    void insertCustomRecommend(CustomRecommendVO customRecommend);
    int updateCustomRecommend(CustomRecommendVO customRecommend);
    int deleteCustomRecommend(String email, String code);
}
