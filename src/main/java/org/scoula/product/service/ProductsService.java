package org.scoula.product.service;

import java.util.List;

import org.scoula.product.dto.MortgageLoanDto;
import org.scoula.product.dto.SavingsDepositsDto;
import org.scoula.product.dto.TimeDepositsDto;
import org.scoula.product.mapper.MortgageLoanMapper;
import org.scoula.product.mapper.SavingsDepositsMapper;
import org.scoula.product.mapper.TimeDepositsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Spring 서비스 빈으로 등록
public class ProductsService {

	@Autowired
	private TimeDepositsMapper timeDepositsMapper; // 예금 Mapper 주입

	@Autowired
	private SavingsDepositsMapper savingsDepositsMapper; // 적금 Mapper 주입

	@Autowired
	private MortgageLoanMapper mortgageLoanMapper; // 주택담보대출 Mapper 주입

	/**
	 * 모든 예금 상품 정보를 조회합니다.
	 * @return 모든 예금 상품의 DTO 리스트
	 */
	public List<TimeDepositsDto> getAllTimeDeposits() {
		return timeDepositsMapper.findAllTimeDeposits();
	}

	/**
	 * 모든 적금 상품 정보를 조회합니다.
	 * @return 모든 적금 상품의 DTO 리스트
	 */
	public List<SavingsDepositsDto> getAllSavingsDeposits() {
		return savingsDepositsMapper.findAllSavingsDeposits();
	}

	/**
	 * 모든 주택담보대출 상품 정보를 조회합니다.
	 * @return 모든 주택담보대출 상품의 DTO 리스트
	 */
	public List<MortgageLoanDto> getAllMortgageLoans() {
		return mortgageLoanMapper.findAllMortgageLoans();
	}

	// public TimeDepositsDTO getTimeDepositsDetail(String finPrdtCd) {
	//     // 상세 정보를 가져오는 Mapper 메서드 호출
	// }
}
