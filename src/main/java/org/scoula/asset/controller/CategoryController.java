package org.scoula.asset.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.dto.CategoryDTO;
import org.scoula.asset.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CategoryDTO> getCategoryByCode(@PathVariable String code) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @PostMapping
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}")
    public ResponseEntity<Void> updateCategory(@PathVariable String code, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setCode(code);
        categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String code) {
        categoryService.deleteCategory(code);
        return ResponseEntity.ok().build();
    }
}
