package org.scoula.recommend.service;

import lombok.RequiredArgsConstructor;
import org.scoula.recommend.dto.CustomRecommendDTO;
import org.scoula.recommend.mapper.CustomRecommendMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomRecommendServiceImpl implements CustomRecommendService {

    private final CustomRecommendMapper customRecommendMapper;

    @Override
    public List<CustomRecommendDTO> getCustomRecommendsByEmail(String email) {
        return customRecommendMapper.getCustomRecommendsByEmail(email).stream()
                .map(CustomRecommendDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public void addCustomRecommend(CustomRecommendDTO customRecommendDTO) {
        customRecommendMapper.insertCustomRecommend(customRecommendDTO.toVO());
    }

    @Override
    public void updateCustomRecommend(CustomRecommendDTO customRecommendDTO) {
        if (customRecommendMapper.updateCustomRecommend(customRecommendDTO.toVO()) == 0) {
            throw new NoSuchElementException("CustomRecommend not found for email: " + customRecommendDTO.getEmail() + " and prdtId: " + customRecommendDTO.getPrdtId());
        }
    }

    @Override
    public void deleteCustomRecommend(String email, String prdtId) {
        if (customRecommendMapper.deleteCustomRecommend(email, prdtId) == 0) {
            throw new NoSuchElementException("CustomRecommend not found for email: " + email + " and prdtId: " + prdtId);
        }
    }
}
