package org.scoula.product.service;

import lombok.RequiredArgsConstructor;
import org.scoula.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductMapper productMapper;

	/**
	 * 상품 코드로 상품 이름을 조회합니다.
	 * @param finPrdtCd 찾고자 하는 상품 코드
	 * @return 상품 이름
	 * @throws NoSuchElementException 해당 코드를 가진 상품이 없을 경우
	 */
	@Override
	public String getProductNameByCode(String finPrdtCd) {
		// 1. 매퍼를 호출하여 DB에서 상품 이름을 조회합니다.
		String productName = productMapper.findNameByCode(finPrdtCd);

		// 2. 조회 결과가 null일 경우(상품이 없을 경우) 예외를 발생시킵니다.
		if (productName == null) {
			throw new NoSuchElementException("Product not found with code: " + finPrdtCd);
		}

		// 3. 조회된 상품 이름을 반환합니다.
		return productName;

        /*
        // Optional을 사용한 더 간결한 표현
        return Optional.ofNullable(productMapper.findNameByCode(finPrdtCd))
                       .orElseThrow(() -> new NoSuchElementException("Product not found with code: " + finPrdtCd));
        */
	}
}
