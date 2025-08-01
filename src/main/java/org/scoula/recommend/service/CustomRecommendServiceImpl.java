package org.scoula.recommend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.scoula.product.domain.ProductVo;
import org.scoula.product.service.ProductsService;
import org.scoula.recommend.domain.CustomRecommendVo;
import org.scoula.recommend.dto.CustomRecommendDto;
import org.scoula.recommend.mapper.CustomRecommendMapper;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomRecommendServiceImpl implements CustomRecommendService {

	private final CustomRecommendMapper customRecommendMapper;
	private final UserService userService;
	private final ProductsService productsService;

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
	 * 사용자가 성향이나 자산 정보를 업데이트 할 때 마다 추천상품을 바꿔줘야 하므로,
	 * addCustomRecommned 메서드는 기존 table 데이터의 삭제 후 새로운 추천 상품을 추가하는 방향으로
	 */
	/**
	 * 새로운 추천 정보를 DB에 추가
	 * 사용자가 성향이나 자산 정보를 업데이트 할 때 마다 추천상품을 바꿔줘야 하므로,
	 * addCustomRecommned 메서드는 기존 table 데이터의 삭제 후 새로운 추천 상품을 추가하는 방향으로
	 */
	@Override
	public void addCustomRecommend(String email) {
		// 1. 사용자 정보와 전체 상품 목록을 가져옵니다.
		UserDto user = userService.getUser(email);
		List<ProductVo> products = productsService.getAllProducts();

		// 2. 사용자 정보나 상품 목록이 없으면, 오류를 방지하기 위해 함수를 종료합니다.
		if (user == null || products == null || products.isEmpty()) {
			// 또는 log.warn("추천 상품을 생성할 사용자나 상품 정보가 없습니다."); 등으로 로깅할 수 있습니다.
			return;
		}

		// 3. 기존 추천 목록을 삭제합니다.
		customRecommendMapper.deleteAllProductsByEmail(email);

		List<CustomRecommendVo> recommendVoList = new ArrayList<>();

		// 4. Null-safe하게 사용자 성향/자산 비율을 가져옵니다. null일 경우 0.0을 기본값으로 사용합니다.
		double userTendency = (user.getTendency() != null) ? user.getTendency() : 0.0;
		double userAssetProportion = (user.getAssetProportion() != null) ? user.getAssetProportion() : 0.0;

		for (ProductVo vo : products) {
			// 상품 정보도 null일 수 있으므로 방어적으로 코드를 작성합니다.
			if (vo == null || vo.getFinPrdtCd() == null) {
				continue; // 필수 정보가 없는 상품은 건너뜁니다.
			}

			// Null-safe하게 상품 성향/자산 비율을 가져옵니다.
			double productTendency = (vo.getTendency() != null) ? vo.getTendency() : 0.0;
			double productAssetProportion = (vo.getAssetProportion() != null) ? vo.getAssetProportion() : 0.0;

			// 코사인 유사도 점수를 계산합니다.
			double score = cosineSimilarity(userTendency, userAssetProportion, productTendency, productAssetProportion);

			// 추천 목록에 추가합니다.
			CustomRecommendVo customRecommendVo = new CustomRecommendVo(email, vo.getFinPrdtCd(), String.valueOf(score));
			recommendVoList.add(customRecommendVo);
		}

		// 5. 유사도 점수를 기준으로 내림차순 정렬합니다.
		recommendVoList.sort((a, b) -> {
			// score가 null이거나 숫자가 아닐 경우를 대비합니다.
			try {
				double scoreA = Double.parseDouble(a.getScore());
				double scoreB = Double.parseDouble(b.getScore());
				return Double.compare(scoreB, scoreA);
			} catch (NumberFormatException e) {
				return 0;
			}
		});

		// 6. 상위 8개의 추천 상품을 DB에 저장합니다.
		for (int i = 0; i < Math.min(8, recommendVoList.size()); i++) {
			customRecommendMapper.insertCustomRecommend(recommendVoList.get(i));
		}
	}


	private double cosineSimilarity(double t1, double a1, double t2, double a2) {
		double dot = t1 * t2 + a1 * a2;
		double normA = Math.sqrt(t1 * t1 + a1 * a1);
		double normB = Math.sqrt(t2 * t2 + a2 * a2);
		return (normA == 0 || normB == 0) ? 0 : dot / (normA * normB);
	}
}
