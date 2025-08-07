package org.scoula.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.scoula.product.domain.DepositVo;
import org.scoula.product.domain.FundVo;
import org.scoula.product.domain.ProductVo;
import org.scoula.product.dto.DepositDto;
import org.scoula.product.dto.FundDto;
import org.scoula.product.dto.GoldDto;
import org.scoula.product.dto.MortgageDto;
import org.scoula.product.dto.ProductDto;
import org.scoula.product.dto.SavingDto;
import org.scoula.product.dto.TrustDto;
import org.scoula.product.mapper.ProductMapper;
import org.scoula.product.struct.ProductVoToMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl {
	private final ProductMapper productMapper;

	public Map<String, List<? extends ProductDto>> findAllProduct() {
		List<? extends ProductVo> list = productMapper.findAllProduct();

		List<DepositDto> depositList = new ArrayList<>();
		List<SavingDto> savingList = new ArrayList<>();
		List<MortgageDto> mortgageList = new ArrayList<>();
		List<FundDto> fundList = new ArrayList<>();
		List<GoldDto> goldList = new ArrayList<>();
		List<TrustDto> trustList = new ArrayList<>();

		//VO -> DTO
		for (ProductVo p : list) {
			if (p instanceof ProductVo) {
				//                // 예금 상품 처리 VO -> DTO
				depositList.add(ProductVoToMapper.toDepositDto((DepositVo) p));
			} else if (p instanceof FundVo) {
				//                // 펀드 상품 처리 VO -> DTO
				fundList.add(ProductVoToMapper.toFundDTO((FundVo) p));
			}
		}

		return Map.of(
			"deposit", depositList,
			// "saving", savingList,
			// "mortgage", mortgageList,
			"fund", fundList
			// "gold", goldList,
			// "trust", trustList
		);
	}
}
