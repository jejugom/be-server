package org.scoula.branch.service;

import java.util.List;

import org.scoula.branch.dto.BranchDto;

public interface BranchService {
	List<BranchDto> getAllBranches();

	BranchDto getBranchById(Integer branchName);

	void addBranch(BranchDto branchDto);

	void updateBranch(BranchDto branchDto);

	void deleteBranch(String branchName);

	String getBranchNameById(int branchId);
}
