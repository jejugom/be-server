package org.scoula.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.scoula.product.domain.MortgageLoanDocuments;
import org.scoula.product.domain.SavingsDepositsDocument;
import org.scoula.product.domain.TimeDepositsDocument;
import org.scoula.product.dto.AllProductsResponseDTO;
import org.scoula.product.dto.BaseInfoDTO;
import org.scoula.product.dto.DepositsOptionInfoDTO;
import org.scoula.product.dto.DepositListResponseDTO;
import org.scoula.product.dto.MortgageLoanListResponseDTO;
import org.scoula.product.dto.MortgageLoanOptionInfoDTO;
import org.scoula.product.repository.MortgageLoanRepository;
import org.scoula.product.repository.SavingsDepositsRepository;
import org.scoula.product.repository.TimeDepositsRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {

	private final TimeDepositsRepository timeDepositsRepository;
	private final SavingsDepositsRepository savingsDepositsRepository;
	private final MortgageLoanRepository mortgageLoanRepository;

	/**
	 * 예금, 적금, 주택담보 통합
	 */
	public AllProductsResponseDTO getAllProducts() {
		List<DepositListResponseDTO> timeDeposits = getTimeDepositList();
		List<DepositListResponseDTO> savingsDeposits = getSavingsDepositList();
		List<MortgageLoanListResponseDTO> mortgageLoan = getMortgageLoanList();

		return new AllProductsResponseDTO(timeDeposits, savingsDeposits, mortgageLoan);
	}


	/**
	 * 예금(time-deposits)상품 가져오기
	 * @return List<DepositListResponseDTO>
	 */
	public List<DepositListResponseDTO> getTimeDepositList() {
		List<TimeDepositsDocument> documents = timeDepositsRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<DepositsOptionInfoDTO> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new DepositsOptionInfoDTO(
							option.getSave_trm(),
							option.getIntr_rate(),
							option.getIntr_rate2()
						))
						.collect(Collectors.toList());

					BaseInfoDTO baseInfo = new BaseInfoDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature()
					);

					return new DepositListResponseDTO(baseInfo, base.getSpcl_cnd(), matchedOptions);
				}))
			.collect(Collectors.toList());
	}

	/**
	 * 적금(savings-deposits)상품 가져오기
	 * @return List<DepositListResponseDTO>
	 */
	public List<DepositListResponseDTO> getSavingsDepositList() {
		List<SavingsDepositsDocument> documents = savingsDepositsRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<DepositsOptionInfoDTO> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new DepositsOptionInfoDTO(
							option.getSave_trm(),
							option.getIntr_rate(),
							option.getIntr_rate2()
						))
						.collect(Collectors.toList());

					BaseInfoDTO baseInfo = new BaseInfoDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature()
					);

					return new DepositListResponseDTO(baseInfo, base.getSpcl_cnd(), matchedOptions);
				}))
			.collect(Collectors.toList());
	}

	/**
	 * 주택담보대출(mortgage-loan-products)상품 가져오기
	 * @return List<MortgageLoanListResponseDTO>
	 */
	public List<MortgageLoanListResponseDTO> getMortgageLoanList() {
		List<MortgageLoanDocuments> documents = mortgageLoanRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<MortgageLoanOptionInfoDTO> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new MortgageLoanOptionInfoDTO(
							option.getMrtg_type_nm(),
							option.getRpay_type_nm(),
							option.getLend_rate_type_nm(),
							option.getLend_rate_min(),
							option.getLend_rate_max()
						))
						.collect(Collectors.toList());

					BaseInfoDTO baseInfo = new BaseInfoDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature()
					);

					return new MortgageLoanListResponseDTO(baseInfo, matchedOptions);
				}))
			.collect(Collectors.toList());
	}
}
