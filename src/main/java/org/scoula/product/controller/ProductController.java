package org.scoula.product.controller;

import java.util.List;

import org.scoula.product.domain.TimeDepositDocument;
import org.scoula.product.service.TimeDepositService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final TimeDepositService service;

	@GetMapping
	public List<TimeDepositDocument> getAllTimeDeposits() {
		return service.findAll();
	}
}
