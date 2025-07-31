package org.scoula.recommend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.scoula.recommend.domain.CustomRecommendVo;
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
		List<CustomRecommendVo> recommendList = customRecommendMapper.getCustomRecommendsByEmail(email);

		// 추천 목록이 null이거나 비어있으면 임시 데이터 반환
		if (recommendList == null || recommendList.isEmpty()) {
			CustomRecommendDto temp1 = CustomRecommendDto.builder()
				.finPrdtCd("TEMP001")
				.score("85")
				.build();

			CustomRecommendDto temp2 = CustomRecommendDto.builder()
				.finPrdtCd("TEMP002")
				.score("90")
				.build();

			return List.of(temp1, temp2);
		}

		return recommendList.stream()
			.map(CustomRecommendDto::of)
			.collect(Collectors.toList());
		// return customRecommendMapper.getCustomRecommendsByEmail(email).stream()
		// 	.map(CustomRecommendDto::of)
		// 	.collect(Collectors.toList());
	}

	/**
	 * 새로운 추천 정보를 DB에 추가
	 * @param customRecommendDto
	 */
	@Override
	public void addCustomRecommend(CustomRecommendDto customRecommendDto) {
		customRecommendMapper.insertCustomRecommend(customRecommendDto.toVo());
	}
}
