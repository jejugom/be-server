package org.scoula.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.scoula.user.domain.UserVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
	String email;
	String name;

	public static UserInfoDTO of(UserVO userVO) {
		return new UserInfoDTO(
			userVO.getEmail(),
			userVO.getName()
		);
	}
}
