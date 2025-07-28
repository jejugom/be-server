package org.scoula.asset.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetStatusVo {
	private int assetId;
	private String email;
	private String assetCategoryCode;
	private Long amount;
	private String assetName;
	private String businessType;
}
