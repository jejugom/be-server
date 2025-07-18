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

    @GetMapping("/{code}")
    public ResponseEntity<BranchDTO> getBranchByCode(@PathVariable String code) {
        return ResponseEntity.ok(branchService.getBranchByCode(code));
    }

    @PostMapping
    public ResponseEntity<Void> addBranch(@RequestBody BranchDTO branchDTO) {
        branchService.addBranch(branchDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}")
    public ResponseEntity<Void> updateBranch(@PathVariable String code, @RequestBody BranchDTO branchDTO) {
        branchDTO.setCode(code);
        branchService.updateBranch(branchDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteBranch(@PathVariable String code) {
        branchService.deleteBranch(code);
        return ResponseEntity.ok().build();
    }
}
