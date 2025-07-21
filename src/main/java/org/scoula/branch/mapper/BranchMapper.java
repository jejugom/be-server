package org.scoula.branch.mapper;

import org.scoula.branch.domain.BranchVO;

import java.util.List;

public interface BranchMapper {
    List<BranchVO> getAllBranches();
    BranchVO getBranchByName(String branchName);
    void insertBranch(BranchVO branch);
    int updateBranch(BranchVO branch);
    int deleteBranch(String code);
}
