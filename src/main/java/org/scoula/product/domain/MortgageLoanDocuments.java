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
@Document(collection = "mortgage-loan-products")
public class MortgageLoanDocuments {

	@Id
	private String id;

	private List<MortgageLoanDocuments.Base> baseList;
	private List<MortgageLoanDocuments.Option> optionList;

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
		private String loan_inci_expn; //대출 부대비용
		private String erly_rpay_fee; //중도 상환 수수료
		private String dly_rate; // 연체 이자율
		private String loan_lmt; // 대출한도
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
		private String mrtg_type; //담보유형 코드
		private String mrtg_type_nm; //담보유형
		private String rpay_type; //대출상환유형 코드
		private String rpay_type_nm; //대출상환유형
		private String lend_rate_type; //대출금리유형 코드
		private String lend_rate_type_nm; //대출금리유형
		private String lend_rate_min; //대출금리_최저[소수점2자리]
		private String lend_rate_max; //대출금리_최고[소수점2자리]
		private String lend_rate_avg; //전월 취급 평균금리[소수점2자리]
	}
}
