package org.scoula.View.preference.service;

import org.scoula.View.preference.dto.PreferenceRequestDto;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PreferenceService의 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService{

	private  final UserService userService;
	private final CustomRecommendService customRecommendService;

	/**
	 * 설문 답변을 점수화하여 사용자의 투자 성향(tendency)을 계산하고,
	 * 이를 바탕으로 사용자 정보를 업데이트한 후 맞춤 추천 상품 목록을 갱신합니다.
	 *
	 * @implNote ⚠️ 주의: 현재 switch문에 break문이 없어 각 case가 연달아 실행(fall-through)됩니다.
	 * 예를 들어 q1이 1인 경우, 1, 2, 3, 4번 case가 모두 실행되어 점수가 의도와 다르게 누적될 수 있습니다.
	 * 각 case가 독립적으로 실행되도록 하려면 각 case의 끝에 'break;'를 추가해야 합니다.
	 *
	 * @param requestDto 사용자의 설문 답변이 담긴 DTO
	 * @param userEmail 성향을 설정할 사용자의 이메일
	 */
	@Override
	public void setUserPreference(PreferenceRequestDto requestDto,String userEmail) {
		double startPoint = 0;

		int q1 = requestDto.getQ1();
		int q2 = requestDto.getQ2();
		int q3 = requestDto.getQ3();
		int q4 = requestDto.getQ4();
		int q5 = requestDto.getQ5();

		// ⚠️ 주의: 각 case 끝에 break;가 없어 fall-through가 발생합니다.
		switch (q1){
			case 1 : startPoint+=0.3;
			case 2 : startPoint += 0.15;
			case 3 : startPoint -=0.15;
			case 4 : startPoint -=0.3;
		}
		switch (q2){
			case 1 : startPoint += 0.3;
			case 2 : startPoint -=0;
			case 3 : startPoint -= 0.3;
		}
		switch (q3){
			case 1: startPoint += 0.3;
			case 2 : startPoint -=0.15;
			case 3 : startPoint -=0.3;
		}
		switch (q4){
			case 1 : startPoint += 0.3;
			case 2 : startPoint += 0.15;
			case 3 : startPoint -= 0.15;
			case 4 : startPoint -= 0.3;
		}
		switch (q5){
			case 1 : startPoint += 0.3;
			case 2 : startPoint += 0 ;
			case 3 : startPoint -= 0.3;
		}

		if(startPoint > 1) startPoint = 1;
		if(startPoint < -1) startPoint = -1;
		/**
		 * -1 ~ 1 까지의 범위로 코사인 유사도 값을 내기 때문에, 1보다 크거나 -1 보다 작은 값은 조정
		 */

		// 사용자 정보를 가져와서 계산된 성향 점수를 업데이트합니다.
		UserDto userDto = userService.getUser(userEmail);
		userDto.setTendency(startPoint);
		userService.updateUser(userEmail,userDto);

		// 변경된 성향을 바탕으로 추천 상품 목록을 다시 생성합니다.
		customRecommendService.addCustomRecommend(userEmail);
	}
}