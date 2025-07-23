package org.scoula.security.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProcessor {
	// 액세스 토큰 유효 기간: 1시간
	private static final long ACCESS_TOKEN_VALID_MILISECOND = 1000L * 60 * 60;
	// ✅ 리프레시 토큰 유효 기간: 2주
	private static final long REFRESH_TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 24 * 14;

	private String secretKey = "비밀키는 충반한 길이의 문자열이어야 한다";
	private Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

	// ✅ 기존 generateToken 메서드 이름을 명확하게 변경
	public String generateAccessToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_VALID_MILISECOND)) // 1시간
			.signWith(key)
			.compact();
	}

	// ✅ 리프레시 토큰 생성 메서드 추가
	public String generateRefreshToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_VALID_MILISECOND)) // 2주
			.signWith(key)
			.compact();
	}

	//JWT 검증 (유요기간 검증) - 해석 불가인 경우 예외 발생
	public boolean validateToken(String token) {
		Jws<Claims> claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
		return true;
	}

	/**
	 * JWT에서 사용자 이메일(Subject)을 추출합니다.
	 * 토큰 해석이 불가능한 경우(서명 오류, 만료 등) 예외가 발생합니다.
	 * @param token Access Token 문자열
	 * @return 사용자 이메일
	 */
	public String getUsername(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}
}
