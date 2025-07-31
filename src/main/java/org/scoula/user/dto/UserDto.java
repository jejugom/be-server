package org.scoula.user.dto;

import java.util.Date;

import org.scoula.user.domain.UserVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "사용자 정보 DTO")
public class UserDto {

	@ApiModelProperty(value = "이메일", example = "user@example.com", required = true)
	private String email;

	@ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
	private String userName;

	@ApiModelProperty(value = "휴대폰 번호", example = "010-1234-5678", required = true)
	private String userPhone;

	@ApiModelProperty(value = "생년월일", example = "1990-01-01", required = true)
	private Date birth;

	@ApiModelProperty(value = "연결된 금융기관 ID", example = "connect123")
	private String connectedId;

	@ApiModelProperty(value = "지점 ID", example = "101")
	private Integer branchId;

	@ApiModelProperty(value = "자산 총액", example = "100000000")
	private Long asset;

	@ApiModelProperty(value = "세그먼트", example = "High Net Worth")
	private String segment;

	@ApiModelProperty(value = "첫 번째 파일 이름", example = "profile1.png")
	private String filename1;

	@ApiModelProperty(value = "두 번째 파일 이름", example = "profile2.png")
	private String filename2;

	@ApiModelProperty(value = "투자 성향 (0.0 ~ 1.0)", example = "0.75")
	private Double tendency;

	@ApiModelProperty(value = "자산 구성 비율", example = "0.45")
	private Double assetProportion;

	@ApiModelProperty(value = "소득 구간", example = "5000만원~7000만원")
	private String incomeRange;

	@ApiModelProperty(value = "결혼 여부", example = "true")
	private Boolean maritalStatus;

	// 변환 메서드
	public static UserDto of(UserVo user) {
		return UserDto.builder()
			.email(user.getEmail())
			.userName(user.getUserName())
			.userPhone(user.getUserPhone())
			.birth(user.getBirth())
			.connectedId(user.getConnectedId())
			.branchId(user.getBranchId())
			.asset(user.getAsset())
			.segment(user.getSegment())
			.filename1(user.getFilename1())
			.filename2(user.getFilename2())
			.tendency(user.getTendency())
			.assetProportion(user.getAssetProportion())
			.incomeRange(user.getIncomeRange())
			.maritalStatus(user.getMaritalStatus())
			.build();
	}

	public UserVo toVo() {
		return UserVo.builder()
			.email(email)
			.userName(userName)
			.userPhone(userPhone)
			.birth(birth)
			.connectedId(connectedId)
			.branchId(branchId)
			.asset(asset)
			.segment(segment)
			.filename1(filename1)
			.filename2(filename2)
			.tendency(tendency)
			.assetProportion(assetProportion)
			.incomeRange(incomeRange)
			.maritalStatus(maritalStatus)
			.build();
	}
}
