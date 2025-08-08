package org.scoula.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.scoula.product.domain.DepositVo;
import org.scoula.product.domain.FundVo;
import org.scoula.product.domain.GoldVo;
import org.scoula.product.domain.MortgageVo;
import org.scoula.product.domain.ProductVo;
import org.scoula.product.domain.SavingVo;
import org.scoula.product.domain.TrustVo;
import org.scoula.product.dto.DepositDto;
import org.scoula.product.dto.DepositOptionDto;
import org.scoula.product.dto.DepositSavingOptionDto;
import org.scoula.product.dto.FundDto;
import org.scoula.product.dto.FundOptionDto;
import org.scoula.product.dto.FundSimpleOptionDto;
import org.scoula.product.dto.GoldDto;
import org.scoula.product.dto.MortgageDto;
import org.scoula.product.dto.MortgageOptionDto;
import org.scoula.product.dto.ProductDto;
import org.scoula.product.dto.SavingDto;
import org.scoula.product.dto.SavingOptionDto;
import org.scoula.product.dto.TrustDto;
import org.scoula.product.mapper.ProductMapper;
import org.scoula.product.struct.ProductVoToMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductService {
	private final ProductMapper productMapper;

	/**
	 * 전체 상품 목록으로 조회
	 * @return
	 */
	public Map<String, List<? extends ProductDto>> findAllProducts() {
		List<? extends ProductVo> list = productMapper.findAllProduct();

		List<ProductDto<DepositSavingOptionDto>> depositList = new ArrayList<>();
		List<ProductDto<DepositSavingOptionDto>> savingList = new ArrayList<>();
		List<ProductDto<MortgageOptionDto>> mortgageList = new ArrayList<>();
		List<ProductDto<FundSimpleOptionDto>> fundList = new ArrayList<>();
		List<ProductDto<Void>> goldList = new ArrayList<>();
		List<ProductDto<Void>> trustList = new ArrayList<>();

		//VO -> DTO
		for (ProductVo p : list) {
			if (p instanceof DepositVo) {
				// 예금 상품 처리 VO -> DTO
				depositList.add(ProductVoToMapper.toDepositSimpleDto((DepositVo)p));
			} else if (p instanceof SavingVo) {
				savingList.add(ProductVoToMapper.toSavingSimpleDto((SavingVo)p));
			} else if (p instanceof MortgageVo) {
				mortgageList.add(ProductVoToMapper.toMortgageSimpleDto((MortgageVo)p));
			} else if (p instanceof FundVo) {
				//                // 펀드 상품 처리 VO -> DTO
				fundList.add(ProductVoToMapper.toFundSimpleDto((FundVo) p));
			} else if (p instanceof GoldVo) {
				goldList.add(ProductVoToMapper.toGoldSimpleDto((GoldVo) p));
			} else if (p instanceof TrustVo) {
				trustList.add(ProductVoToMapper.toTrustSimpleDto((TrustVo) p));
			}
		}

		return Map.of(
			"deposit", depositList,
			"saving", savingList,
			"mortgage", mortgageList,
			"fund", fundList,
			"gold", goldList,
			"trust", trustList
		);
	}

	/**
	 * 상품 코드로 상세정보 가져오기
	 * @param finPrdtCd
	 * @return
	 */
	public ProductVo getProductDetail(String finPrdtCd) {
		ProductVo product = productMapper.findProductDetail(finPrdtCd);
		if (product == null) {
			throw new NoSuchElementException("해당 상품을 찾을 수 없습니다: " + finPrdtCd);
		}
		return product;
	}

	@Override
	public String getProductNameByCode(String finPrdtCode) {
		return "";
	}

}
