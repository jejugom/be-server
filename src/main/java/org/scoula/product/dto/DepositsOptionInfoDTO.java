package org.scoula.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositsOptionInfoDTO {
	/**
	 * 예금(timeDeposits)과 적금(savingsDeposits)에서 공통으로 쓰일 option 필드입니다.
	 */
	private String save_trm; //저축 기간 [단위: 개월]
	private Double intr_rate; //저축 그림
	private Double intr_rate2; //최고 우대금리
}
