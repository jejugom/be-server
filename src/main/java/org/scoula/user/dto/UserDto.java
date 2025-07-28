package org.scoula.user.dto;

import java.util.Date;

import org.scoula.user.domain.UserVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String email;
	private String userName;
	private String userPhone;
	private Date birth;
	private String connectedId;
	private Integer branchId;
	private Long asset;
	private String segment;
	private String filename1;
	private String filename2;
	private Double tendency;
	private Double assetProportion;
	private String incomeRange;
	private Boolean martialStatus;

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
			.martialStatus(user.getMartialStatus())
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
			.martialStatus(martialStatus)
			.build();
	}
}
