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
public class AssetDetailVo {
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
