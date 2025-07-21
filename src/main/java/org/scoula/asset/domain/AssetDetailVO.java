package org.scoula.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailVO {
    private int assetId;
    private String email;
    private String assetCategoryCode;
    private Long amount;
    private Date registeredAt;
    private Date endDate;
    private String assetName;
    private String businessType;
    private String categoryAssetCategoryCode;
}
