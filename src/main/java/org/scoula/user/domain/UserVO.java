
package org.scoula.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    private String email;
    private String userName;
    private String userPhone;
    private Date birth;
    private String branchName;
    private String connectedId;
    private String branchBranchName;
}
