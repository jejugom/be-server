package org.scoula.user.dto;

import java.util.List;

import org.scoula.asset.dto.AssetStatusSummaryDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageUserInfoDto {
	private String email;
	private String userName;
	private List<AssetStatusSummaryDto> assetStatus; // 자산 현황 리스트를 내부에 포함
}