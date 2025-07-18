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
    private String name;
    private String phone;
    private Date birth;

    public static UserDTO of(UserVO user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .birth(user.getBirth())
                .build();
    }

    public UserVO toVO() {
        return UserVO.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
    }
}
