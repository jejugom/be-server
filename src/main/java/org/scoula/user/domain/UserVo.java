package org.scoula.user.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo {
	private String email;
	private String userName;
	private String userPhone;
	private Date birth;
	private String branchName;
	private String connectedId;
}
