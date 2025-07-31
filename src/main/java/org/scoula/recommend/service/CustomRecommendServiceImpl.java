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
	 * @param customRecommendDto
	 * 사용자가 성향이나 자산 정보를 업데이트 할 때 마다 추천상품을 바꿔줘야 하므로,
	 * addCustomRecommned 메서드는 기존 table 데이터의 삭제 후 새로운 추천 상품을 추가하는 방향으로
	 */
	@Override
	public void addCustomRecommend(String email) {
		/**
		 * 상품 | 사용자
		 */
		List<ProductVo> products = productsService.getAllProducts();
		UserDto user = userService.getUser(email);
		/**
		 * 유저가 정보를 업데이트 할 때마다 추천 목록을 바꿔줘야 되므로 기존 테이블 정보 삭제
		 */
		customRecommendMapper.deleteAllProductsByEmail(email);

		List<CustomRecommendVo> recommendVoList = new ArrayList<>();

		for(ProductVo vo : products){
			double productTendency = vo.getTendency();
			double productAssetProportion = vo.getAssetProportion();
			double userTendency = user.getTendency();
			double userAssetProportion = user.getAssetProportion();
			double score = cosineSimilarity(userTendency,userAssetProportion,productTendency,productAssetProportion);
			CustomRecommendVo customRecommendVo = new CustomRecommendVo(email,vo.getFinPrdtCd(),score+"");
			recommendVoList.add(customRecommendVo);
		}
		//유사도 내림차순 정렬
		recommendVoList.sort((a, b) -> Double.compare(Double.parseDouble(b.getScore()), Double.parseDouble(a.getScore())));
		//  상위 8개 저장
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
