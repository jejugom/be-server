package org.scoula.branch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.scoula.branch.domain.BranchVo;

public interface BranchMapper {
	List<BranchVo> getAllBranches();

	BranchVo getBranchById(@Param("branchId") Integer branchId);

	void insertBranch(BranchVo branch);

	int updateBranch(BranchVo branch);

	int deleteBranch(String code);

	/**
	 * ID를 통해 지점 이름을 조회
	 * @param branchId
	 * @return String branchName
	 */
	String findBranchNameById(int branchId);
}
