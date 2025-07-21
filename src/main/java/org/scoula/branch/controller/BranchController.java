package org.scoula.branch.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.branch.dto.BranchDTO;
import org.scoula.branch.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{branchName}")
    public ResponseEntity<BranchDTO> getBranchByCode(@PathVariable String branchName) {
        return ResponseEntity.ok(branchService.getBranchByName(branchName));
    }

    @PostMapping
    public ResponseEntity<Void> addBranch(@RequestBody BranchDTO branchDTO) {
        branchService.addBranch(branchDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{branchName}")
    public ResponseEntity<Void> updateBranch(@PathVariable String branchName, @RequestBody BranchDTO branchDTO) {
        branchDTO.setBranchName(branchName);
        branchService.updateBranch(branchDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{branchName}")
    public ResponseEntity<Void> deleteBranch(@PathVariable String branchName) {
        branchService.deleteBranch(branchName);
        return ResponseEntity.ok().build();
    }
}
