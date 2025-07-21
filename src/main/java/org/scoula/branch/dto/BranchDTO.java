package org.scoula.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.branch.domain.BranchVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDTO {
    private String branchName;
    private String branchPhone;
    private String addressName;
    private String roadAddressName;
    private String x;
    private String y;
    private String distance;

    public static BranchDTO of(BranchVO branch) {
        return BranchDTO.builder()
                .branchName(branch.getBranchName())
                .branchPhone(branch.getBranchPhone())
                .addressName(branch.getAddressName())
                .roadAddressName(branch.getRoadAddressName())
                .x(branch.getX())
                .y(branch.getY())
                .distance(branch.getDistance())
                .build();
    }

    public BranchVO toVO() {
        return BranchVO.builder()
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
