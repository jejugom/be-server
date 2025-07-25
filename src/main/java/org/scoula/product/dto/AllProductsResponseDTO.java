package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProductsResponseDTO {

	/**
	 * 예금/적금/주택담보대출 상품 데이터를 한번에 묶어서 넘기기 위한 DTO
	 */
	private List<TimeDepositsDTO> timeDeposits;
	private List<SavingsDepositsDTO> savingDeposits;
	private List<MortgageLoanDTO> mortgageLoan;
}
