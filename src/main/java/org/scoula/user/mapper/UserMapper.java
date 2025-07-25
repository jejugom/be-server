package org.scoula.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.user.domain.UserVo;

@Mapper
public interface UserMapper {
	UserVo findByEmail(@Param("email") String email);

	int save(UserVo user);

	int update(UserVo user);

	int delete(String email);

	int updateConnectedId(@Param("email") String email, @Param("connectedId") String connectedId);

	int updateBranchName(@Param("email") String email, @Param("branchName") String branchName);

	String findBranchNameByEmail(@Param("email") String email);



}
