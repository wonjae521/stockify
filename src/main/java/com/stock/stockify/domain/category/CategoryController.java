package com.stock.stockify.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ 카테고리 등록
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequest request) {
        categoryService.createCategory(request.getName());
        return ResponseEntity.ok("카테고리 생성 완료");
    }

    // ✅ 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // ✅ 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("카테고리 삭제 완료");
    }
}
