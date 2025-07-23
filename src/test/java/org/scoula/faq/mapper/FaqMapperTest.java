package org.scoula.faq.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.faq.domain.FaqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// @ExtendWith: JUnit5에서 Spring의 테스트 기능을 사용하도록 확장합니다.
@ExtendWith(SpringExtension.class)
// @ContextConfiguration: 테스트에 필요한 설정 정보를 담고 있는 클래스를 지정합니다.
// RootConfig.class에는 DataSource, SqlSessionFactory 등의 Bean 설정이 있어야 합니다.
@ContextConfiguration(classes = {org.scoula.config.RootConfig.class})
class FaqMapperTest {

	// @Autowired: Spring 컨테이너가 생성하고 관리하는 FaqMapper Bean을 자동으로 주입받습니다.
	@Autowired
	private FaqMapper faqMapper;

	@Test
	@DisplayName("전체 FAQ 목록을 성공적으로 조회합니다.")
	void testGetAllFaqs() {
		// when: Mapper의 getAllFaqs 메소드를 직접 호출하여 DB에서 데이터를 가져옵니다.
		List<FaqVo> faqs = faqMapper.getAllFaqs();

		// then: JUnit 5 기본 단정문으로 실행 결과를 검증합니다.
		assertNotNull(faqs, "결과 리스트는 null이 아니어야 합니다.");
		// 데이터베이스에 미리 삽입된 데이터의 총 개수를 기반으로 검증합니다.
		// 예를 들어 25개의 데이터가 있다면, assertEquals(25, faqs.size()); 와 같이 작성할 수 있습니다.
	}

	@Test
	@DisplayName("존재하는 ID로 FAQ를 성공적으로 조회합니다.")
	void testGetFaqById_found() {
		// given: 데이터베이스에 존재해야 하는 ID를 지정합니다.
		int faqId = 1;

		// when: Mapper의 getFaqById 메소드를 직접 호출하여 ID 1번 데이터를 조회합니다.
		FaqVo faq = faqMapper.getFaqById(faqId);

		// then: 조회된 데이터가 null이 아니며, ID가 일치하는지 확인합니다.
		assertNotNull(faq, "ID 1번 데이터는 존재해야 합니다.");
		assertEquals(faqId, faq.getFaqId());
	}

	@Test
	@DisplayName("존재하지 않는 ID로 FAQ 조회 시 null을 반환합니다.")
	void testGetFaqById_notFound() {
		// given: 데이터베이스에 존재하지 않을 것으로 예상되는 ID를 지정합니다.
		int nonExistentId = 9999;

		// when: Mapper의 getFaqById 메소드를 직접 호출하여 존재하지 않는 ID로 조회합니다.
		FaqVo faq = faqMapper.getFaqById(nonExistentId);

		// then: Mapper는 데이터가 없을 때 보통 null을 반환하므로, null인지 검증합니다.
		assertNull(faq, "존재하지 않는 ID로 조회 시 결과는 null이어야 합니다.");
	}
}
