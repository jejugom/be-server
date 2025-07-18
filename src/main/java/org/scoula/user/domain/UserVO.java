
package org.scoula.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    private String email;
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private String gender;
    private List<AuthVO> authList;
}
