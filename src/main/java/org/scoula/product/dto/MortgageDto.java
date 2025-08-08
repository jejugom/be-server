package org.scoula.product.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
public class MortgageDto extends ProductDetailDto<MortgageOptionDto> {

	private String loanInciExpn; //대출 부대비용
	private String erlyRpayFee; //중도상환 수수료
	private String dlyRate; //연체 이자율
	private String loanLmt; //대출 한도
}
