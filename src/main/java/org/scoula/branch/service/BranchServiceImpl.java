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
	public BranchDto getBranchById(Integer branchId) {
		return Optional.ofNullable(branchMapper.getBranchById(branchId))
			.map(BranchDto::of)
			.orElseThrow(() -> new NoSuchElementException("Branch not found with name: " + branchId));
	}

	/**
	 * branchId로 branchName을 조회하는 메소드
	 */
	@Override
	public String getBranchNameById(int branchId) {
		// mapper를 통해 branchId로 이름을 조회하고, 결과가 없을 경우 예외를 던집니다.
		return Optional.ofNullable(branchMapper.findBranchNameById(branchId))
			.orElseThrow(() -> new NoSuchElementException("Branch not found with id: " + branchId));
	}
}
