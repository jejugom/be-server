package org.scoula.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.asset.domain.AssetDetailVO;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailDTO {
    private Integer id;
    private String email;
    private String code;
    private BigDecimal amount;
    private Date registeredAt;
    private Date endDate;
    private String assetName;
    private String businessType;

    public static AssetDetailDTO of(AssetDetailVO assetDetail) {
        return AssetDetailDTO.builder()
                .id(assetDetail.getId())
                .email(assetDetail.getEmail())
                .code(assetDetail.getCode())
                .amount(assetDetail.getAmount())
                .registeredAt(assetDetail.getRegisteredAt())
                .endDate(assetDetail.getEndDate())
                .assetName(assetDetail.getAssetName())
                .businessType(assetDetail.getBusinessType())
                .build();
    }

    public AssetDetailVO toVO() {
        return AssetDetailVO.builder()
                .id(id)
                .email(email)
                .code(code)
                .amount(amount)
                .registeredAt(registeredAt)
                .endDate(endDate)
                .assetName(assetName)
                .businessType(businessType)
                .build();
    }
}
