package org.scoula.user.dto;

import org.scoula.user.domain.UserVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
	String email;
	String userName;

	public static UserInfoDto of(UserVo userVO) {
		return new UserInfoDto(
			userVO.getEmail(),
			userVO.getUserName()
		);
	}
}
