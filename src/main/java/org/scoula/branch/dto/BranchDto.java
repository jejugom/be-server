package org.scoula.branch.dto;

import org.scoula.branch.domain.BranchVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {
	private String branchName;
	private String branchPhone;
	private String addressName;
	private String roadAddressName;
	private String x;
	private String y;
	private String distance;

	public static BranchDto of(BranchVo branch) {
		return BranchDto.builder()
			.branchName(branch.getBranchName())
			.branchPhone(branch.getBranchPhone())
			.addressName(branch.getAddressName())
			.roadAddressName(branch.getRoadAddressName())
			.x(branch.getX())
			.y(branch.getY())
			.distance(branch.getDistance())
			.build();
	}

	public BranchVo toVo() {
		return BranchVo.builder()
			.branchName(branchName)
			.branchPhone(branchPhone)
			.addressName(addressName)
			.roadAddressName(roadAddressName)
			.x(x)
			.y(y)
			.distance(distance)
			.build();
	}
}
