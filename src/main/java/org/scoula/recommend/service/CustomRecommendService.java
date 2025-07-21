package org.scoula.recommend.service;

import org.scoula.recommend.dto.CustomRecommendDTO;

import java.util.List;

public interface CustomRecommendService {
    List<CustomRecommendDTO> getCustomRecommendsByEmail(String email);
    void addCustomRecommend(CustomRecommendDTO customRecommendDTO);
    void updateCustomRecommend(CustomRecommendDTO customRecommendDTO);
    void deleteCustomRecommend(String email, String prdtId);
}
