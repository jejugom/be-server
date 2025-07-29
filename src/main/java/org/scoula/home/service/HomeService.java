package org.scoula.home.service;

import java.util.List;

import org.scoula.home.dto.HomeResponseDto;
import org.scoula.home.dto.RecommendationDto;
import org.scoula.home.dto.UserSummary;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	private final UserService userService;

	@Transactional
	public HomeResponseDto getHomeData(String userEmail) {
		// 추천은 항상 포함
		List<RecommendationDto> hardcodedRecommends = List.of(
			new RecommendationDto("KB001", "KB Star 정기예금", "6개월 예치, 안정적인 이자 수익", 3.2,4.0),
			new RecommendationDto("KB002", "KB 골드 연금", "장기 투자 적합 연금 상품", 4.0,5.5),
			new RecommendationDto("KB003", "KB 투자형 펀드", "리스크 있지만 수익률 기대", 5.5,10.2)
		);

		if (userEmail == null) {
			// 비로그인 사용자는 userSummary 없이 리턴
			return HomeResponseDto.builder()
				.recommandTop3(hardcodedRecommends)
				.build();
		}

		// 로그인 사용자 정보 예시 (실제 서비스에서 DB 조회)
		UserSummary summary = UserSummary.builder()
			.name(userService.getUser(userEmail).getUserName()) // 예시
			.asset(userService.getUser(userEmail).getAsset())
			.build();

		return HomeResponseDto.builder()
			.userSummary(summary)
			.recommandTop3(hardcodedRecommends)
			.build();
	}
}
