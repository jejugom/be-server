package org.scoula.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.auth.dto.RefreshTokenDto;

@Mapper
public interface RefreshTokenMapper {
	/**
	 * 리프레시 토큰을 저장하거나 이미 존재하면 갱신합니다.
	 * @param refreshToken 저장할 토큰 정보
	 */
	void saveRefreshToken(RefreshTokenDto refreshToken);

	// 이메일과 provider로 토큰을 조회하는 메서드 추가
	// provider 파라미터 삭제
	RefreshTokenDto findTokenByUserEmail(@Param("email") String email);

	int deleteByEmail(@Param("email") String email);
}
