package org.scoula.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 수증자별 세금 상세 정보 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientTaxDetailDto {
	private String recipientName;   // 수증자 이름
	private long totalGiftAmount;   // 해당 수증자가 받은 총 증여액
	private long estimatedTax;      // 해당 수증자의 예상 증여세
}