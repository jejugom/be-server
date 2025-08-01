package org.scoula.branch.controller;

import java.util.List;

import org.scoula.branch.dto.BranchDto;
import org.scoula.branch.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

	private final BranchService branchService;

	@GetMapping
	public ResponseEntity<List<BranchDto>> getAllBranches() {
		return ResponseEntity.ok(branchService.getAllBranches());
	}

	@GetMapping("/{branchName}")
	public ResponseEntity<BranchDto> getBranchById(@PathVariable Integer branchName) {
		return ResponseEntity.ok(branchService.getBranchById(branchName));
	}

	@PostMapping
	public ResponseEntity<Void> addBranch(@RequestBody BranchDto branchDto) {
		branchService.addBranch(branchDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{branchName}")
	public ResponseEntity<Void> updateBranch(@PathVariable String branchName, @RequestBody BranchDto branchDto) {
		branchDto.setBranchName(branchName);
		branchService.updateBranch(branchDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{branchName}")
	public ResponseEntity<Void> deleteBranch(@PathVariable String branchName) {
		branchService.deleteBranch(branchName);
		return ResponseEntity.ok().build();
	}
}
