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
    private String code;
    private String name;
    private String tel;
    private String address;
    private String x;
    private String y;

    public static BranchDTO of(BranchVO branch) {
        return BranchDTO.builder()
                .code(branch.getCode())
                .name(branch.getName())
                .tel(branch.getTel())
                .address(branch.getAddress())
                .x(branch.getX())
                .y(branch.getY())
                .build();
    }

    public BranchVO toVO() {
        return BranchVO.builder()
                .code(code)
                .name(name)
                .tel(tel)
                .address(address)
                .x(x)
                .y(y)
                .build();
    }
}
