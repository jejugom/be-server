package org.scoula.user.dto;

import org.scoula.user.domain.UserVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "사용자 요약 정보 DTO (이메일, 이름만 포함)")
public class UserInfoDto {

	@ApiModelProperty(value = "이메일", example = "user@example.com", required = true)
	private String email;

	@ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
	private String userName;

	public static UserInfoDto of(UserVo userVO) {
		return new UserInfoDto(
			userVO.getEmail(),
			userVO.getUserName()
		);
	}
}
