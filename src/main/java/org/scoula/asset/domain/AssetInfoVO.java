package org.scoula.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetInfoVO {
    private String email;
    private BigDecimal asset;
    private String segment;
    private String filename1;
    private String filename2;
}
