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
public class TimeDepositDocument {

	@Id
	private String id;

	private List<Base> baseList;
	private List<Option> optionList;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Base {
		private String dcls_month;
		private String fin_co_no;
		private String fin_prdt_cd;
		private String kor_co_nm;
		private String fin_prdt_nm;
		private String prdt_feature;
		private String join_way;
		private String mtrt_int;
		private String spcl_cnd;
		private String join_deny;
		private String join_member;
		private String etc_note;
		private String max_limit;
		private String dcls_strt_day;
		private String dcls_end_day;
		private String fin_co_subm_day;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Option {
		private String dcls_month;
		private String fin_co_no;
		private String fin_prdt_cd;
		private String intr_rate_type;
		private String intr_rate_type_nm;
		private String save_trm;
		private Double intr_rate;
		private Double intr_rate2;
	}
}
