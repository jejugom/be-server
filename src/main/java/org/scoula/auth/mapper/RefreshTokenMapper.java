package org.scoula.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.auth.dto.RefreshTokenDto;

@Mapper
public interface RefreshTokenMapper {
	/**
	 * 리프레시 토큰을 저장하거나 이미 존재하면 갱신합니다.
	 * @param refreshToken 저장할 토큰 정보
	 */
	void saveRefreshToken(RefreshTokenDto refreshToken);
}
