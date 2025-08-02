package org.scoula.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.security.config.SecurityConfig;
import org.scoula.user.domain.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
@Transactional
public class UserMapperTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	@DisplayName("사용자 저장 및 이메일로 조회 테스트")
	void testSaveAndFindByEmail() {
		// given - @Builder를 사용하여 테스트용 사용자 객체 생성
		UserVo testUser = UserVo.builder()
			.email("testuser@example.com")
			.userName("테스트유저")
			.userPhone("010-1234-5678")
			.birth(new Date())
			.segment("직장인")
			.build();

		// when - 사용자 저장
		int saveCount = userMapper.save(testUser);

		// then - 저장이 성공했는지 확인
		assertEquals(1, saveCount, "사용자 저장이 성공해야 합니다.");

		// when - 저장된 사용자를 이메일로 조회
		UserVo foundUser = userMapper.findByEmail("testuser@example.com");

		// then - 조회된 사용자의 정보가 일치하는지 확인
		assertNotNull(foundUser, "조회된 사용자가 null이 아니어야 합니다.");
		assertEquals("테스트유저", foundUser.getUserName(), "사용자 이름(userName)이 일치해야 합니다.");
		assertEquals("010-1234-5678", foundUser.getUserPhone(), "사용자 전화번호(userPhone)가 일치해야 합니다.");
	}

	@Test
	@DisplayName("사용자 정보 수정 테스트")
	void testUpdate() {
		// given - 먼저 테스트용 사용자를 저장
		UserVo user = UserVo.builder()
			.email("updateuser@example.com")
			.userName("수정전이름")
			.userPhone("010-0000-0000")
			.build();
		userMapper.save(user);

		// when - 사용자 정보 수정 (이름과 전화번호 변경)
		user.setUserName("수정후이름");
		user.setUserPhone("010-9999-9999");
		int updateCount = userMapper.update(user);

		// then - 수정이 성공했는지 확인
		assertEquals(1, updateCount, "사용자 정보 수정이 성공해야 합니다.");

		// then - 수정된 정보가 DB에 올바르게 반영되었는지 다시 조회하여 확인
		UserVo updatedUser = userMapper.findByEmail("updateuser@example.com");
		assertEquals("수정후이름", updatedUser.getUserName(), "이름이 성공적으로 수정되어야 합니다.");
		assertEquals("010-9999-9999", updatedUser.getUserPhone(), "전화번호가 성공적으로 수정되어야 합니다.");
	}

	@Test
	@DisplayName("이메일로 사용자 삭제 테스트")
	void testDeleteByEmail() {
		// given - 삭제할 사용자 저장
		UserVo userToDelete = UserVo.builder()
			.email("deleteuser@example.com")
			.userName("삭제될사용자")
			.build();
		userMapper.save(userToDelete);

		// when - 사용자 삭제
		int deleteCount = userMapper.deleteByEmail("deleteuser@example.com");

		// then - 삭제가 성공했는지 확인
		assertEquals(1, deleteCount, "사용자 삭제가 성공해야 합니다.");

		// then - 삭제된 사용자가 더 이상 조회되지 않는지 확인
		UserVo foundUser = userMapper.findByEmail("deleteuser@example.com");
		assertNull(foundUser, "삭제된 사용자는 조회되지 않아야 합니다.");
	}
}
