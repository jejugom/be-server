package org.scoula.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 *
 * */
@Data
@AllArgsConstructor
public class TokenRefreshResponseDto {
	private String accessToken;
	private String refreshToken;
}
