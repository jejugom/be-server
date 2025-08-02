package org.scoula.auth.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.auth.dto.RefreshTokenDto;
import org.scoula.config.RootConfig;
import org.scoula.security.config.SecurityConfig;
import org.scoula.user.domain.UserVo;
import org.scoula.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
@Transactional
public class RefreshTokenMapperTests {

	@Autowired
	private RefreshTokenMapper refreshTokenMapper;

	@Autowired
	private UserMapper userMapper;

	private UserVo testUser;

	@BeforeEach
	void setUp() {
		// Arrange (Given) - 각 테스트 전에 부모 데이터인 User를 먼저 생성
		this.testUser = UserVo.builder()
			.email("test-user@example.com")
			.userName("테스트유저")
			.build();
		userMapper.save(testUser);
	}

	@Test
	@DisplayName("RefreshToken 저장 및 조회 테스트")
	void testSaveAndFind() {
		// Arrange (Given) - DTO 빌더로 테스트용 토큰 객체 생성
		RefreshTokenDto testToken = RefreshTokenDto.builder()
			.email(testUser.getEmail())
			.tokenValue("dummy-token-value-12345")
			.expiresAt(LocalDateTime.now().plusDays(14))
			.build();

		// Act (When) - 토큰 저장
		refreshTokenMapper.saveRefreshToken(testToken);

		// Act (When) - 저장된 토큰 조회
		RefreshTokenDto foundToken = refreshTokenMapper.findTokenByUserEmail(testUser.getEmail());

		// Assert (Then) - 조회 결과 검증
		assertNotNull(foundToken, "저장된 토큰은 조회 시 null이 아니어야 합니다.");
		assertEquals(testUser.getEmail(), foundToken.getEmail());
		assertEquals("dummy-token-value-12345", foundToken.getTokenValue());
		assertNotNull(foundToken.getExpiresAt());
	}

	@Test
	@DisplayName("RefreshToken 갱신 테스트")
	void testUpdate() {
		// Arrange (Given) - 초기 토큰 저장
		RefreshTokenDto originalToken = RefreshTokenDto.builder()
			.email(testUser.getEmail())
			.tokenValue("original-token")
			.expiresAt(LocalDateTime.now().plusDays(14))
			.build();
		refreshTokenMapper.saveRefreshToken(originalToken);

		// Arrange (Given) - 갱신할 토큰 정보
		RefreshTokenDto updatedToken = RefreshTokenDto.builder()
			.email(testUser.getEmail())
			.tokenValue("updated-token-98765")
			.expiresAt(LocalDateTime.now().plusDays(20))
			.build();

		// Act (When) - 동일한 이메일로 다시 저장하여 갱신
		refreshTokenMapper.saveRefreshToken(updatedToken);

		// Assert (Then) - 갱신된 정보 확인
		RefreshTokenDto foundToken = refreshTokenMapper.findTokenByUserEmail(testUser.getEmail());
		assertNotNull(foundToken);
		assertEquals("updated-token-98765", foundToken.getTokenValue(), "토큰 값은 새로운 값으로 갱신되어야 합니다.");
	}

	@Test
	@DisplayName("이메일로 RefreshToken 삭제 테스트")
	void testDelete() {
		// Arrange (Given) - 삭제할 토큰 저장
		RefreshTokenDto tokenToDelete = RefreshTokenDto.builder()
			.email(testUser.getEmail())
			.tokenValue("token-to-be-deleted")
			.expiresAt(LocalDateTime.now().plusDays(14))
			.build();
		refreshTokenMapper.saveRefreshToken(tokenToDelete);

		// Act (When) - 토큰 삭제
		int deleteCount = refreshTokenMapper.deleteByEmail(testUser.getEmail());

		// Assert (Then) - 삭제 결과 및 최종 상태 확인
		assertEquals(1, deleteCount, "한 개의 행이 삭제되어야 합니다.");
		RefreshTokenDto foundToken = refreshTokenMapper.findTokenByUserEmail(testUser.getEmail());
		assertNull(foundToken, "삭제된 토큰은 조회되지 않아야 합니다.");
	}
}
