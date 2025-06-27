package com.stock.stockify.domain.category;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.NotFoundException;
import com.stock.stockify.global.exception.PermissionDeniedException;
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
    public void createCategory(CategoryRequest request) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "INVENTORY_WRITE");
        User owner = UserContext.getCurrentUser();

        // 권한 검사
        if (!owner.isEmailVerified()) {
            throw new PermissionDeniedException("이메일 인증이 완료되어야 카테고리를 생성할 수 있습니다.");
        }

        // 동일 이름 중복 방지 (owner 기준)
        boolean exists = categoryRepository.existsByNameAndOwner(request.getName(), owner);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .owner(owner)
                .build();

        categoryRepository.save(category);
    }

    /** 로그인한 사용자의 ADMIN 기준 카테고리 전체 조회 */
    public List<Category> getAllCategories() {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "INVENTORY_VIEW");
        User owner = user.getAdminOwner();
        return categoryRepository.findByOwner(owner);
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
    public void deleteCategory(Long categoryId) {
        User currentUser = UserContext.getCurrentUser();
        User owner = UserContext.getCurrentUser();
        if (!owner.isEmailVerified()) {
            throw new PermissionDeniedException("이메일 인증이 완료되어야 카테고리를 생성할 수 있습니다.");
        }
        permissionChecker.check(currentUser.getId(), "INVENTORY_DELETE");
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        // 삭제 권한 확인 (ID 기준이 아니라 ADMIN 소유자 기준으로 검사)
        Long categoryOwnerId = category.getOwner().getAdmin() != null ? category.getOwner().getAdmin().getId() : category.getOwner().getId();
        Long userAdminId = currentUser.getAdmin() != null ? currentUser.getAdmin().getId() : currentUser.getId();

        if (!categoryOwnerId.equals(userAdminId)) {
            throw new IllegalArgumentException("본인 소유의 카테고리만 삭제할 수 있습니다.");
        }

        categoryRepository.delete(category);
    }

    /** 카테고리 이름 수정 및 관련 재고 항목 업데이트 */
    public void updateCategoryName(Long categoryId, CategoryRequest request) {
        User currentUser = UserContext.getCurrentUser();
        User owner = UserContext.getCurrentUser();
        if (!owner.isEmailVerified()) {
            throw new PermissionDeniedException("이메일 인증이 완료되어야 카테고리를 생성할 수 있습니다.");
        }
        permissionChecker.check(currentUser.getId(), "INVENTORY_UPDATE");

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        // 수정 권한 확인 (owner ID 기준)
        if (!category.getOwner().getId().equals(currentUser.getId())) {
            throw new PermissionDeniedException("카테고리를 수정할 권한이 없습니다.");
        }

        category.setName(request.getName());
        categoryRepository.save(category);
    }
}
