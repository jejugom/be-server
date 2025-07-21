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

    @GetMapping("/{assetCategoryCode}")
    public ResponseEntity<CategoryDTO> getCategoryByCode(@PathVariable String assetCategoryCode) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(assetCategoryCode));
    }

    @PostMapping
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{assetCategoryCode}")
    public ResponseEntity<Void> updateCategory(@PathVariable String assetCategoryCode, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setAssetCategoryCode(assetCategoryCode);
        categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{assetCategoryCode}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String assetCategoryCode) {
        categoryService.deleteCategory(assetCategoryCode);
        return ResponseEntity.ok().build();
    }
}
