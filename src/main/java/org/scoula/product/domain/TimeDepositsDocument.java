package org.scoula.product.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "time-deposits")
public class TimeDepositsDocument {

	@Id
	private String id;

	private List<Base> baseList;
	private List<Option> optionList;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Base {
		private String dcls_month; //공시 제출월
		private String fin_co_no; //금융회사 코드
		private String kor_co_nm; //금융회사 명
		private String fin_prdt_cd; //금융상품 코드
		private String fin_prdt_nm; //금융 상품명
		private String prdt_feature; //금융 상품 특징
		private String join_way; //가입방법
		private String mtrt_int; //만기 후 이자율
		private String spcl_cnd; //우대조건
		private String join_deny; //가입제한 ex)1:제한없음, 2:서민전용. 3:일부제한
		private String join_member; //가입대상
		private String etc_note; //기타 유의사항
		private String max_limit; //최고한도
		private String dcls_strt_day; //공시 시작일
		private String dcls_end_day; //공시 종료일
		private String fin_co_subm_day; //금융회사 제출일
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Option {
		private String dcls_month; //공시 제출월
		private String fin_co_no; //금융회사 코드
		private String fin_prdt_cd; //금융상품 코드
		private String intr_rate_type; //저축 금리 유형
		private String intr_rate_type_nm; //저축 금리 유형명
		private String save_trm; //저축 기간[단위: 개월]
		private Double intr_rate; //저축 금리[소수점 2자리]
		private Double intr_rate2; //저축 금리[소수점 2자리]
	}
}
