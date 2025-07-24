package org.scoula.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeDepositDetailResponseDTO {

	private BaseInfoDTO baseInfo;
	private List<DepositsOptionInfoDTO> optionList;

	// 상세에서만 필요한 필드
	// 추후 지정
}
