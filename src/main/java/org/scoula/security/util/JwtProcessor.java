package org.scoula.security.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT(Json Web Token)의 생성, 검증, 정보 추출을 담당하는 유틸리티 클래스
 * - RS256(비대칭키) 적용
 * - 토큰 저장 위치는 클라이언트에서 HttpOnly + Secure 쿠키 사용 권장
 */
@Component
public class JwtProcessor {

	// 액세스 토큰 유효 기간: 1시간
	private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60;
	// 리프레시 토큰 유효 기간: 2주
	private static final long REFRESH_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 14;

	private final PrivateKey privateKey;
	private final PublicKey publicKey;

	public JwtProcessor(
		@Value("${jwt.private-key}") String privateKeyStr,
		@Value("${jwt.public-key}") String publicKeyStr
	) throws Exception {
		// 환경변수에서 가져온 Base64 문자열 → Key 객체로 변환
		PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
		this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(privateSpec);

		X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
		this.publicKey = KeyFactory.getInstance("RSA").generatePublic(publicSpec);
	}

	/** 액세스 토큰 생성 */
	public String generateAccessToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_MILLISECOND))
			.signWith(privateKey, SignatureAlgorithm.RS256)
			.compact();
	}

	/** 리프레시 토큰 생성 */
	public String generateRefreshToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_MILLISECOND))
			.signWith(privateKey, SignatureAlgorithm.RS256)
			.compact();
	}

	/** 클레임 포함 액세스 토큰 생성 */
	public String generateAccessToken(String subject, Map<String, Object> claims) {
		return Jwts.builder()
			.setSubject(subject)
			.addClaims(claims)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_MILLISECOND))
			.signWith(privateKey, SignatureAlgorithm.RS256)
			.compact();
	}

	/** JWT 검증 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** JWT에서 사용자 이메일(Subject) 추출 */
	public String getUsername(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}
}
