package org.scoula.preference.service;

import org.scoula.preference.dto.PreferenceRequestDto;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService{

	private  final UserService userService;

	@Override
	public void setUserPreference(PreferenceRequestDto requestDto,String userEmail) {
		int startPoint = 0;

		int q1 = requestDto.getQ1();
		int q2 = requestDto.getQ2();
		int q3 = requestDto.getQ3();
		int q4 = requestDto.getQ4();
		int q5 = requestDto.getQ5();
		switch (q1){
			case 1 : startPoint+=0.4;
			case 2 : startPoint += 0.2;
			case 3 : startPoint -=0.2;
			case 4 : startPoint -=0.4;
		}
		switch (q2){
			case 1 : startPoint += 0.3;
			case 2 : startPoint -=0;
			case 3 : startPoint -= 0.3;
		}
		switch (q3){
			case 1: startPoint += 0.3;
			case 2 : startPoint -=0.2;
			case 3 : startPoint -=0.3;
		}
		switch (q4){
			case 1 : startPoint += 0.4;
			case 2 : startPoint += 0.2;
			case 3 : startPoint -= 0.2;
			case 4 : startPoint -= 0.4;
		}
		switch (q5){
			case 1 : startPoint += 0.4;
			case 2 : startPoint += 0 ;
			case 3 : startPoint -= 0.4;
		}

		if(startPoint > 1) startPoint = 1;
		if(startPoint < -1) startPoint = 1;
		/**
		 * -1 ~ 1 까지의 범위로 코사인 유사도 값을 내기 때문에, 1보다 크거나 -1 보다 작은 값은 조정
		 */
		UserDto userDto = userService.getUser(userEmail);
		userDto.setTendency((double)startPoint);
		userService.updateUser(userEmail,userDto);
		//유저 질문 토대로 Tendency 수정 .
	}
}
