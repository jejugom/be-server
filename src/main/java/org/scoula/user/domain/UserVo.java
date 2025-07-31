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
	private String connectedId;
	private Integer branchId;
	private Long asset;
	private String segment;
	private String filename1;
	private String filename2;
	private Double tendency;
	private Double assetProportion;
	private String incomeRange;
}
