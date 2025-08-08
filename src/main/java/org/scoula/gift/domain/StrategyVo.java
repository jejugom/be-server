package org.scoula.gift.domain;

import lombok.Data;

@Data
public class StrategyVo {
	private int strategyId;
	private String strategyCode;
	private String ruleCategory;
	private String message;
}