package org.scoula.statistics.domain;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 클릭 로그 관련 VO(Value Object) 클래스
 */
@ApiModel(value = "클릭 로그 VO", description = "DB의 product_click_log 테이블과 매핑되는 핵심 객체")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductClickLogVo {
	private Long id;
	private String finPrdtCd;
	private String email;
	private String triggeredBy;
	private LocalDateTime createdAt;
}
