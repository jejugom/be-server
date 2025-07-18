package org.scoula.branch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchVO {
    private String code;
    private String name;
    private String tel;
    private String address;
    private String x;
    private String y;
}
