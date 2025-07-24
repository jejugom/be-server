package org.scoula.auth.dto;

import lombok.Data;

/**
 * Refresh Token을 재발급 받기 위한 Dto
 * */
@Data
public class RefreshTokenRequestDto {
	private String refreshToken;
}
