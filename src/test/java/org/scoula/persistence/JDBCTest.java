package org.scoula.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JDBCTest {

	// 모든 테스트 실행 전 한 번만 실행
	@BeforeAll
	public static void setup() {
		try {
			// MySQL JDBC 드라이버 로드
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("JDBC 드라이버 연결이 된다.")
	public void testConnection() {
		// 데이터베이스 연결 URL
		String url = "jdbc:mysql://localhost:3306/jeju_gom";

		// try-with-resources로 자동 자원 해제
		try (Connection con = DriverManager.getConnection(url, "root", "1234")) {
			log.info("=== JDBC 연결 성공 ===");
			log.info("연결 객체: {}", con);
		} catch (Exception e) {
			fail("데이터베이스 연결 실패: " + e.getMessage());
		}
	}
}