package org.scoula.auth.dto;

import org.scoula.user.dto.UserInfoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResultDto {
	String token;
	UserInfoDto user;
}
