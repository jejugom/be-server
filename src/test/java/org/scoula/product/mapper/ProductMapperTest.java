package org.scoula.product.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.product.domain.DepositOptionVo;
import org.scoula.product.domain.DepositVo;
import org.scoula.product.domain.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RootConfig.class)
@Slf4j
class ProductMapperTest {
	@Autowired
	private ProductMapper productMapper;

	@Test
	void findNameByCode() {
	}

	@Test
	void findCategoryByFinPrdtCd() {
	}

	@Test
	void getAllProduct() {
	}

	@Test
	void findAllProductTest() {
		List<? extends ProductVo> list = productMapper.findAllProduct();
		DepositVo expectedDepositVo1 = new DepositVo();
		expectedDepositVo1.setFinPrdtCd("DP1000014");
		assertTrue(list.contains(expectedDepositVo1));

		DepositVo expectedDepositVo2 = new DepositVo();
		expectedDepositVo2.setFinPrdtCd("DP240000");
		assertTrue(list.contains(expectedDepositVo2));

		list.forEach(p -> {
			switch (p.getFinPrdtCd()) {
				case "DP01000014": //예금 테스트
					assertEquals("일반정기예금", p.getFinPrdtNm());
					assertTrue(p.getOptionList().size() == 1);
					DepositOptionVo option = assertInstanceOf(DepositOptionVo.class, p.getOptionList().get(0));
					assertEquals("고정금리", option.getIntrRateTypeNm());
					assertEquals(2.25F, option.getIntrRate());
					break;
				case "DP240000":
					assertEquals("정기예금", p.getFinPrdtNm());
					assertTrue(p.getOptionList().size() == 10);
					break;
				case "1A81": //펀드 테스트
					assertEquals("KB국민참여정책형뉴딜", p.getFinPrdtNm());
					assertTrue(p.getOptionList().size() == 1);
					break;
			}
		});
	}
}
