package org.scoula.product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FundDailyReturnVo {
	private Long id;
	private String fundCode;
	private LocalDate recordDate;
	private BigDecimal returnRate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
