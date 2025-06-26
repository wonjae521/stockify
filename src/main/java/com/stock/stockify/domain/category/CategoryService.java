package com.stock.stockify.domain.category;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.NotFoundException;
import com.stock.stockify.global.security.UserContext;
import com.stock.stockify.global.auth.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Category 관련 비즈니스 로직 처리 서비스 클래스
 * - ADMIN 기준 카테고리 생성, 조회, 삭제
 * - 사용자 권한 기반 접근 제한
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PermissionChecker permissionChecker;

    /** 카테고리 등록 */
    public void createCategory(String name) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "CATEGORY_CREATE");
        User owner = user.getAdminOwner();

        Category category = Category.builder()
                .name(name)
                .owner(owner)
                .build();
        categoryRepository.save(category);
    }

    /** 로그인한 사용자의 ADMIN 기준 카테고리 전체 조회 */
    public List<Category> getAllCategories() {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "CATEGORY_VIEW");
        User owner = user.getAdminOwner();
        return categoryRepository.findAll().stream()
                .filter(c -> c.getOwner().equals(owner))
                .toList();
    }

    /** 이름으로 카테고리 조회 (권한 없음 - 내부용) */
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다: " + name));
    }

    /** 이름 + 소유자로 카테고리 조회 (내부 로직용) */
    public Category getCategoryByNameAndOwner(String name, User owner) {
        return categoryRepository.findByNameAndOwner(name, owner)
                .orElseThrow(() -> new NotFoundException("해당 카테고리를 찾을 수 없습니다."));
    }

    /** 카테고리 삭제 */
    public void deleteCategory(Long id) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "CATEGORY_DELETE");

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));

        if (!category.getOwner().equals(user.getAdminOwner())) {
            throw new RuntimeException("본인 소유의 카테고리만 삭제할 수 있습니다.");
        }

        categoryRepository.deleteById(id);
    }
}
