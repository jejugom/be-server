package org.scoula.branch.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.branch.dto.BranchDto;
import org.scoula.branch.mapper.BranchMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

	private final BranchMapper branchMapper;

	@Override
	public List<BranchDto> getAllBranches() {
		return branchMapper.getAllBranches().stream()
			.map(BranchDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public BranchDto getBranchByName(String branchName) {
		return Optional.ofNullable(branchMapper.getBranchByName(branchName))
			.map(BranchDto::of)
			.orElseThrow(() -> new NoSuchElementException("Branch not found with name: " + branchName));
	}

	@Override
	public void addBranch(BranchDto branchDto) {
		branchMapper.insertBranch(branchDto.toVo());
	}

	@Override
	public void updateBranch(BranchDto branchDto) {
		if (branchMapper.updateBranch(branchDto.toVo()) == 0) {
			throw new NoSuchElementException("Branch not found with name: " + branchDto.getBranchName());
		}
	}

	@Override
	public void deleteBranch(String branchName) {
		if (branchMapper.deleteBranch(branchName) == 0) {
			throw new NoSuchElementException("Branch not found with name: " + branchName);
		}
	}
}
