package org.scoula.branch.service;

import lombok.RequiredArgsConstructor;
import org.scoula.branch.dto.BranchDTO;
import org.scoula.branch.mapper.BranchMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;

    @Override
    public List<BranchDTO> getAllBranches() {
        return branchMapper.getAllBranches().stream()
                .map(BranchDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDTO getBranchByCode(String code) {
        return Optional.ofNullable(branchMapper.getBranchByCode(code))
                .map(BranchDTO::of)
                .orElseThrow(() -> new NoSuchElementException("Branch not found with code: " + code));
    }

    @Override
    public void addBranch(BranchDTO branchDTO) {
        branchMapper.insertBranch(branchDTO.toVO());
    }

    @Override
    public void updateBranch(BranchDTO branchDTO) {
        if (branchMapper.updateBranch(branchDTO.toVO()) == 0) {
            throw new NoSuchElementException("Branch not found with code: " + branchDTO.getCode());
        }
    }

    @Override
    public void deleteBranch(String code) {
        if (branchMapper.deleteBranch(code) == 0) {
            throw new NoSuchElementException("Branch not found with code: " + code);
        }
    }
}
