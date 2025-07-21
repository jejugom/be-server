package org.scoula.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.user.domain.UserVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String userName;
    private String userPhone;
    private Date birth;
    private String branchName;
    private String connectedId;
    private String branchBranchName;

    public static UserDTO of(UserVO user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .birth(user.getBirth())
                .branchName(user.getBranchName())
                .connectedId(user.getConnectedId())
                .branchBranchName(user.getBranchBranchName())
                .build();
    }

    public UserVO toVO() {
        return UserVO.builder()
                .email(email)
                .userName(userName)
                .userPhone(userPhone)
                .birth(birth)
                .branchName(branchName)
                .connectedId(connectedId)
                .branchBranchName(branchBranchName)
                .build();
    }
}
