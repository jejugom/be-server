package org.scoula.branch.service;

import org.scoula.branch.dto.BranchDTO;

import java.util.List;

public interface BranchService {
    List<BranchDTO> getAllBranches();
    BranchDTO getBranchByName(String branchName);
    void addBranch(BranchDTO branchDTO);
    void updateBranch(BranchDTO branchDTO);
    void deleteBranch(String branchName);
}
