package org.scoula.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * vo.Mortgage(주택담보대출)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MortgageVo extends ProductVo<MortgageOptionVo> {

	private String loanInciExpn; //대출 부대비용
	private String erlyRpayFee; //중도상환 수수료
	private String dlyRate; //연체 이자율
	private String loanLmt; //대출 한도
}
