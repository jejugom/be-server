package org.scoula.branch.mapper;

import java.util.List;

import org.scoula.branch.domain.BranchVo;

public interface BranchMapper {
	List<BranchVo> getAllBranches();

	BranchVo getBranchByName(String branchName);

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
