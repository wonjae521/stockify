package com.stock.stockify.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ✅ 카테고리 등록
    public void createCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        categoryRepository.save(category);
    }

    // ✅ 카테고리 전체 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ 카테고리 이름으로 조회
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다: " + name));
    }

    // ✅ 카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
