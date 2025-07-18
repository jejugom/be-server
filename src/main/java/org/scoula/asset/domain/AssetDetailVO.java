package org.scoula.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailVO {
    private Integer id;
    private String email;
    private String code;
    private BigDecimal amount;
    private Date registeredAt;
    private Date endDate;
    private String assetName;
    private String businessType;
}
