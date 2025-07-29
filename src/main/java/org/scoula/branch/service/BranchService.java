package org.scoula.branch.service;

import java.util.List;

import org.scoula.branch.dto.BranchDto;

public interface BranchService {
	List<BranchDto> getAllBranches();

	BranchDto getBranchByName(String branchName);

	void addBranch(BranchDto branchDto);

	void updateBranch(BranchDto branchDto);

	void deleteBranch(String branchName);

	String getBranchNameById(int branchId);
}
