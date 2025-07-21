package org.scoula.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.asset.domain.AssetInfoVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetInfoDTO {
    private String email;
    private Long asset;
    private String segment;
    private String filename1;
    private String filename2;

    public static AssetInfoDTO from(AssetInfoVO assetInfo) {
        return AssetInfoDTO.builder()
                .email(assetInfo.getEmail())
                .asset(assetInfo.getAsset())
                .segment(assetInfo.getSegment())
                .filename1(assetInfo.getFilename1())
                .filename2(assetInfo.getFilename2())
                .build();
    }

    public AssetInfoVO toVO() {
        return AssetInfoVO.builder()
                .email(email)
                .asset(asset)
                .segment(segment)
                .filename1(filename1)
                .filename2(filename2)
                .build();
    }
}
