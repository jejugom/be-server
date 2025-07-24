package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageLoanListResponseDTO {

	/**
	 * 주택담보대출(mortgageLoan)상품 목록 보기에서 쓰일 DTO 입니다.
	 */
	private BaseInfoDTO baseInfo;
	private List<MortgageLoanOptionInfoDTO> optionList; //하나의 상품에 대해 여러 개의 옵션이 있을 수 있기에 List<E>로 설정
}
