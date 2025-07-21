package org.scoula.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.asset.domain.AssetDetailVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailDTO {
    private int assetId;
    private String email;
    private String assetCategoryCode;
    private Long amount;
    private Date registeredAt;
    private Date endDate;
    private String assetName;
    private String businessType;
    private String categoryAssetCategoryCode;

    public static AssetDetailDTO of(AssetDetailVO assetDetail) {
        return AssetDetailDTO.builder()
                .assetId(assetDetail.getAssetId())
                .email(assetDetail.getEmail())
                .assetCategoryCode(assetDetail.getAssetCategoryCode())
                .amount(assetDetail.getAmount())
                .registeredAt(assetDetail.getRegisteredAt())
                .endDate(assetDetail.getEndDate())
                .assetName(assetDetail.getAssetName())
                .businessType(assetDetail.getBusinessType())
                .categoryAssetCategoryCode(assetDetail.getCategoryAssetCategoryCode())
                .build();
    }

    public AssetDetailVO toVO() {
        return AssetDetailVO.builder()
                .assetId(assetId)
                .email(email)
                .assetCategoryCode(assetCategoryCode)
                .amount(amount)
                .registeredAt(registeredAt)
                .endDate(endDate)
                .assetName(assetName)
                .businessType(businessType)
                .categoryAssetCategoryCode(categoryAssetCategoryCode)
                .build();
    }
}
