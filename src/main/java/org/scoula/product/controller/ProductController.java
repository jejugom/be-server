package org.scoula.product.controller;

import java.util.List;

import org.scoula.product.dto.AllProductsResponseDTO;
import org.scoula.product.dto.MortgageLoanDTO;
import org.scoula.product.dto.SavingsDepositsDTO;
import org.scoula.product.dto.TimeDepositsDTO;
import org.scoula.product.service.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/retirement/products")
public class ProductController {

	private final ProductsService productsService;

	/**
	 * 모든 상품 보여주는 메서드
	 * @return ResponseEntity<AllProductsResponseDTO>
	 */
	@GetMapping
	public ResponseEntity<AllProductsResponseDTO> getAllProducts() {
		AllProductsResponseDTO response = productsService.getAllProducts();
		return ResponseEntity.ok(response);
	}

	/**
	 * 예금 상품 보여주는 메서드
	 * @return List<DepositListResponseDTO>
	 */
	@GetMapping("/time-deposits")
	public List<TimeDepositsDTO> getAllTimeDeposits() {
		return productsService.getTimeDepositList();
	}

	/**
	 * 적금 상품 보여주는 메서드
	 * @return List<DepositListResponseDTO>
	 */
	@GetMapping("/savings-deposits")
	public List<SavingsDepositsDTO> getAllSavingsDeposits() {
		return productsService.getSavingsDepositList();
	}

	/**
	 * 주택 담보 대출 상품 보여주는 메서드
	 */
	@GetMapping("/mortgage-loan")
	public List<MortgageLoanDTO> getAllMortgageLoan() {
		return productsService.getMortgageLoanList();
	}
}
