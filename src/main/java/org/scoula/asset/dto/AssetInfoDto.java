package org.scoula.asset.dto;

import org.scoula.asset.domain.AssetInfoVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetInfoDto {
	private String email;
	private Long asset;
	private String segment;
	private String filename1;
	private String filename2;

	public static AssetInfoDto from(AssetInfoVo assetInfo) {
		return AssetInfoDto.builder()
			.email(assetInfo.getEmail())
			.asset(assetInfo.getAsset())
			.segment(assetInfo.getSegment())
			.filename1(assetInfo.getFilename1())
			.filename2(assetInfo.getFilename2())
			.build();
	}

	public AssetInfoVo toVo() {
		return AssetInfoVo.builder()
			.email(email)
			.asset(asset)
			.segment(segment)
			.filename1(filename1)
			.filename2(filename2)
			.build();
	}
}
