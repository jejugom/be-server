package org.scoula.product.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.product.domain.MortgageLoanDocuments;
import org.scoula.product.domain.SavingsDepositsDocument;
import org.scoula.product.domain.TimeDepositsDocument;
import org.scoula.product.dto.AllProductsResponseDTO;
import org.scoula.product.dto.MortgageLoanDTO;
import org.scoula.product.dto.SavingsDepositsDTO;
import org.scoula.product.dto.TimeDepositsDTO;
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
		List<TimeDepositsDTO> timeDeposits = getTimeDepositList();
		List<SavingsDepositsDTO> savingsDeposits = getSavingsDepositList();
		List<MortgageLoanDTO> mortgageLoan = getMortgageLoanList();

		return new AllProductsResponseDTO(timeDeposits, savingsDeposits, mortgageLoan);
	}

	/**
	 * 예금(time-deposits)상품 가져오기
	 * @return List<DepositListResponseDTO>
	 */
	public List<TimeDepositsDTO> getTimeDepositList() {
		List<TimeDepositsDocument> documents = timeDepositsRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<TimeDepositsDTO.OptionList> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new TimeDepositsDTO.OptionList(
							option.getSave_trm(),
							option.getIntr_rate(),
							option.getIntr_rate2()
						))
						.collect(Collectors.toList());

					return new TimeDepositsDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature(),
						matchedOptions
					);
				}))
			.collect(Collectors.toList());
	}

	/**
	 * 적금(savings-deposits)상품 가져오기
	 * @return List<DepositListResponseDTO>
	 */
	public List<SavingsDepositsDTO> getSavingsDepositList() {
		List<SavingsDepositsDocument> documents = savingsDepositsRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<SavingsDepositsDTO.OptionList> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new SavingsDepositsDTO.OptionList(
							option.getSave_trm(),
							option.getIntr_rate(),
							option.getIntr_rate2()
						))
						.collect(Collectors.toList());

					return new SavingsDepositsDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature(),
						matchedOptions
					);
				}))
			.collect(Collectors.toList());
	}

	/**
	 * 주택담보대출(mortgage-loan-products)상품 가져오기
	 * @return List<MortgageLoanListResponseDTO>
	 */
	public List<MortgageLoanDTO> getMortgageLoanList() {
		List<MortgageLoanDocuments> documents = mortgageLoanRepository.findAll();

		return documents.stream()
			.flatMap(doc -> doc.getBaseList().stream()
				.map(base -> {
					List<MortgageLoanDTO.OptionList> matchedOptions = doc.getOptionList().stream()
						.filter(option -> option.getFin_prdt_cd().equals(base.getFin_prdt_cd()))
						.map(option -> new MortgageLoanDTO.OptionList(
							option.getMrtg_type_nm(),
							option.getRpay_type_nm(),
							option.getLend_rate_type_nm(),
							option.getLend_rate_min(),
							option.getLend_rate_max()
						))
						.collect(Collectors.toList());

					return new MortgageLoanDTO(
						base.getFin_prdt_cd(),
						base.getFin_prdt_nm(),
						base.getPrdt_feature(),
						matchedOptions
					);
				}))
			.collect(Collectors.toList());
	}

	/**
	 * 상품 코드로 상품 이름을 조회합니다.
	 * 예금, 적금, 주택담보대출 순으로 검색합니다.
	 * @param finPrdtCd 찾고자 하는 상품 코드
	 * @return 상품 이름
	 * @throws NoSuchElementException 해당 코드를 가진 상품이 없을 경우
	 */
	public String getProductNameByCode(String finPrdtCd) {
		// 1. 예금 상품에서 검색
		Optional<String> timeDepositName = timeDepositsRepository.findByBaseListFinPrdtCd(finPrdtCd)
			.flatMap(doc -> doc.getBaseList().stream()
				.filter(base -> base.getFin_prdt_cd().equals(finPrdtCd))
				.findFirst()
				// 'BaseList' -> 'Base'로 수정
				.map(TimeDepositsDocument.Base::getFin_prdt_nm));

		if (timeDepositName.isPresent()) {
			return timeDepositName.get();
		}

		// 2. 적금 상품에서 검색
		Optional<String> savingsDepositName = savingsDepositsRepository.findByBaseListFinPrdtCd(finPrdtCd)
			.flatMap(doc -> doc.getBaseList().stream()
				.filter(base -> base.getFin_prdt_cd().equals(finPrdtCd))
				.findFirst()
				// 'BaseList' -> 'Base'로 수정
				.map(SavingsDepositsDocument.Base::getFin_prdt_nm));

		if (savingsDepositName.isPresent()) {
			return savingsDepositName.get();
		}

		// 3. 주택담보대출 상품에서 검색
		Optional<String> mortgageLoanName = mortgageLoanRepository.findByBaseListFinPrdtCd(finPrdtCd)
			.flatMap(doc -> doc.getBaseList().stream()
				.filter(base -> base.getFin_prdt_cd().equals(finPrdtCd))
				.findFirst()
				// 'BaseList' -> 'Base'로 수정
				.map(MortgageLoanDocuments.Base::getFin_prdt_nm));

		// 4. 최종적으로 찾은 이름을 반환하거나, 없으면 예외 발생
		return mortgageLoanName.orElseThrow(() ->
			new NoSuchElementException("Product not found with code: " + finPrdtCd)
		);
	}
}
