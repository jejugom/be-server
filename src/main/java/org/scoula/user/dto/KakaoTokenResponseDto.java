package org.scoula.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenResponseDto {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("expires_in")
	private Integer expiresIn;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("refresh_token_expires_in")
	private Integer refreshTokenExpiresIn;
}
