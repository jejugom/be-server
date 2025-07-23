package org.scoula.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetInfoVo {
	private String email;
	private Long asset;
	private String segment;
	private String filename1;
	private String filename2;
}
