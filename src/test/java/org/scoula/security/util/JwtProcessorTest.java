package org.scoula.security.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	RootConfig.class, SecurityConfig.class
})
@Log4j2
class JwtProcessorTest {
	@Autowired
	JwtProcessor jwtProcessor;

	private String testToken;

	@BeforeEach
	void setup() {
		String email = "test@example.com";
		testToken = jwtProcessor.generateToken(email);
	}

	@Test
	void generateToken(){
		assertNotNull(testToken);
		log.info("Generated Token: " + testToken);
	}

	@Test
	void getUsername(){
		String email = jwtProcessor.getUsername(testToken);
		log.info("Username from Token: " + email);
		assertEquals("test@example.com", email);
	}

	@Test
	void validToken(){
		boolean isValid = jwtProcessor.validateToken(testToken);
		log.info("Token is valid: " + isValid);
		assertTrue(isValid);
	}
}