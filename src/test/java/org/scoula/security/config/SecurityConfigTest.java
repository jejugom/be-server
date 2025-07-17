package org.scoula.security.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	RootConfig.class,
	SecurityConfig.class
})
@Log4j2
class SecurityConfigTest {
	@Autowired
	private PasswordEncoder pwE;
	@Test
	public void testEncode(){
		String str = "1234";
		String enstr = pwE.encode(str); //암호화
		log.info("passwd : " + enstr);

		String enStr2 = pwE.encode(str);
		log.info("pw" +enStr2);

		log.info("match : " + pwE.matches(str,enstr));
		log.info("match : " + pwE.matches(str,enStr2));
	}
}