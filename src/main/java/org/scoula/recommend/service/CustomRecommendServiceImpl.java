package org.scoula.recommend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.scoula.recommend.dto.CustomRecommendDto;
import org.scoula.recommend.mapper.CustomRecommendMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomRecommendServiceImpl implements CustomRecommendService {

	private final CustomRecommendMapper customRecommendMapper;

	@Override
	public List<CustomRecommendDto> getCustomRecommendsByEmail(String email) {
		return customRecommendMapper.getCustomRecommendsByEmail(email).stream()
			.map(CustomRecommendDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public void addCustomRecommend(CustomRecommendDto customRecommendDto) {
		customRecommendMapper.insertCustomRecommend(customRecommendDto.toVo());
	}

	@Override
	public void updateCustomRecommend(CustomRecommendDto customRecommendDto) {
		if (customRecommendMapper.updateCustomRecommend(customRecommendDto.toVo()) == 0) {
			throw new NoSuchElementException(
				"CustomRecommend not found for email: " + customRecommendDto.getEmail() + " and prdtId: "
					+ customRecommendDto.getPrdtId());
		}
	}

	@Override
	public void deleteCustomRecommend(String email, String prdtId) {
		if (customRecommendMapper.deleteCustomRecommend(email, prdtId) == 0) {
			throw new NoSuchElementException(
				"CustomRecommend not found for email: " + email + " and prdtId: " + prdtId);
		}
	}
}
