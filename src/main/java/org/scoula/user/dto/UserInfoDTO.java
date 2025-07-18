package org.scoula.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.scoula.user.domain.UserVO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
	String email;
	String name;
	List<String> roles;

	public static UserInfoDTO of(UserVO userVO) {
		return new UserInfoDTO(
			userVO.getEmail(),
			userVO.getName(),
			userVO.getAuthList().stream()
				.map(a -> a.getAuth())
				.toList()
		);
	}
}
