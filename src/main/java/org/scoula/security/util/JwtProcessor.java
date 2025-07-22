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
	private static final long TOKEN_VALID_MILISECOND = 1000L * 60 * 60; // 1시간
	private String secretKey = "비밀키는 충반한 길이의 문자열이어야 한다";
	private Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

	//private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); - 운영 시 사용

	//JWT 생성
	public String generateToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() + TOKEN_VALID_MILISECOND))
			.signWith(key)
			.compact();
	}

	//JWT subject(username) 추출 - 해석 불가인 경우 예욍
	//예외 ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
	//illegalArgumentException
	public String getUsername(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	//JWT 검증 (유요기간 검증) - 해석 불가인 경우 예외 발생
	public boolean validateToken(String token) {
		Jws<Claims> claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
		return true;
	}
}
