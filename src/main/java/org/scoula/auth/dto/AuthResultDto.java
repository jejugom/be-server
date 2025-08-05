package org.scoula.auth.dto;

import org.scoula.user.dto.UserInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "인증 결과 DTO", description = "로그인 또는 토큰 재발급 성공 시 반환되는 데이터")
public class AuthResultDto {

	@ApiModelProperty(value = "발급된 JWT Access Token", required = true, example = "eyJhbGcIiOiJ0ZXN0QG...")
	String token;

	@ApiModelProperty(value = "인증된 사용자 정보", required = true)
	UserInfoDto user;
}
