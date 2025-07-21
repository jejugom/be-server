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
    private String branchName;
    private String branchPhone;
    private String addressName;
    private String roadAddressName;
    private String x;
    private String y;
    private String distance;
}
