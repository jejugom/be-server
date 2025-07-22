package org.scoula.branch.mapper;

import java.util.List;

import org.scoula.branch.domain.BranchVo;

public interface BranchMapper {
	List<BranchVo> getAllBranches();

	BranchVo getBranchByName(String branchName);

	void insertBranch(BranchVo branch);

	int updateBranch(BranchVo branch);

	int deleteBranch(String code);
}
