package org.scoula.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.user.domain.UserVo;

/**
 * 사용자 정보 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스
 */
@Mapper
public interface UserMapper {
	/**
	 * 이메일로 사용자를 조회합니다.
	 * @param email 조회할 사용자의 이메일
	 * @return UserVo 객체
	 */
	UserVo findByEmail(@Param("email") String email);

	/**
	 * 새로운 사용자를 저장합니다.
	 * @param user 저장할 사용자 정보
	 * @return 저장된 행의 수
	 */
	int save(UserVo user);

	/**
	 * 기존 사용자 정보를 수정합니다.
	 * @param user 수정할 사용자 정보
	 * @return 수정된 행의 수
	 */
	int update(UserVo user);

	/**
	 * 이메일로 사용자를 삭제합니다.
	 * @param email 삭제할 사용자의 이메일
	 * @return 삭제된 행의 수
	 */
	int deleteByEmail(@Param("email") String email);

	/**
	 * 사용자의 마이데이터 연동 ID를 수정합니다.
	 * @param email 사용자 이메일
	 * @param connectedId 수정할 연동 ID
	 * @return 수정된 행의 수
	 */
	int updateConnectedId(@Param("email") String email, @Param("connectedId") String connectedId);

	/**
	 * 사용자의 선호 지점 ID를 수정합니다.
	 * @param email 사용자 이메일
	 * @param branchId 수정할 지점 ID
	 * @return 수정된 행의 수
	 */
	int updateBranchId(@Param("email") String email, @Param("branchId") Integer branchId);
}
