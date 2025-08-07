package org.scoula.user.dto;

import java.util.Date;

import org.scoula.user.domain.UserVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "사용자 기본 정보 조회 응답 DTO")
public class UserInfoResponseDto {

	@ApiModelProperty(value = "사용자 이메일", example = "user@example.com")
	private String email;

	@ApiModelProperty(value = "사용자 이름", example = "김스코")
	private String userName;

	@ApiModelProperty(value = "사용자 전화번호", example = "010-1234-5678")
	private String userPhone;

	@ApiModelProperty(value = "생년월일", example = "1995-08-07")
	private Date birth;

	public static UserInfoResponseDto of(UserVo user) {
		return UserInfoResponseDto.builder()
			.email(user.getEmail())
			.userName(user.getUserName())
			.userPhone(user.getUserPhone())
			.birth(user.getBirth())
			.build();
	}
}