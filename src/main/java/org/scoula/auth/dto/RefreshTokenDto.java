package org.scoula.auth.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDto {
	private String userEmail;
	private String provider;
	private String tokenValue;
	private LocalDateTime expiresAt;
}
