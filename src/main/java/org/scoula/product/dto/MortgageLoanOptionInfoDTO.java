package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageLoanOptionInfoDTO {

	/**
	 * 주택담보대출(mortgageLoan)상품 목록 보기에서 쓰일 option 필드입니다.
	 */
	private String mrtg_type_nm; //담보유형
	private String rpay_type_nm; //대출상환유형
	private String lend_rate_type_nm; //대출금리유형
	private String lend_rate_min; //대출금리_최저[소수점2자리]
	private String lend_rate_max; //대출금리_최고[소수점2자리]
}
