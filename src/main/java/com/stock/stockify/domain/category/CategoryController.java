package com.stock.stockify.domain.category;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.security.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Category 관련 API 요청을 처리하는 컨트롤러
 * - 카테고리 등록, 조회, 삭제 기능 제공
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    // 카테고리 등록
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequest request) {
        User owner = UserContext.getCurrentUser();
        if (categoryRepository.existsByNameAndOwner(request.getName(), owner)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        categoryService.createCategory(request);
        return ResponseEntity.ok("카테고리 생성 완료");
    }

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> response = categories.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리 삭제 완료");
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategoryName(@PathVariable Long categoryId,
                                                     @RequestBody CategoryRequest request) {
        categoryService.updateCategoryName(categoryId, request);
        return ResponseEntity.ok("카테고리 이름 수정 완료");
    }
}
