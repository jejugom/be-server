import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
public class JdbcTests {

	@Autowired
	private DataSource dataSource;

	@Test
	@DisplayName("JDBC 드라이버 연결이 된다.")
	public void testConnection() {
		try (Connection con = dataSource.getConnection()) {
			log.info(con);
			log.info("DDDDD");
		} catch (Exception e) {
			log.error("JDBC connection error: ", e);
			fail("JDBC connection failed: " + e.getMessage());
		}
	}
}
