package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.PermissionDeniedException;
import com.stock.stockify.global.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final ItemTagRepository itemTagRepository;
    private final TagRuleRepository tagRuleRepository;
    private final TagActivityLogRepository tagActivityLogRepository;

    /**
     * 태그 생성
     */
    @Transactional
    public void createTag(TagRequest request) {
        User user = UserContext.getCurrentUser();

        if (!user.isEmailVerified()) {
            throw new PermissionDeniedException("이메일 인증 후 사용 가능합니다.");
        }

        boolean exists = tagRepository.existsByNameAndOwnerAndIsDeletedFalse(request.getName(), user);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 태그 이름입니다.");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(user)
                .isDeleted(false)
                .build();

        tagRepository.save(tag);
    }

    /**
     * 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TagResponse> getTags() {
        User user = UserContext.getCurrentUser();
        return tagRepository.findByOwnerAndIsDeletedFalse(user).stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 단일 태그 조회
     */
    @Transactional(readOnly = true)
    public TagResponse getTag(Long tagId) {
        User user = UserContext.getCurrentUser();
        Tag tag = tagRepository.findByIdAndIsDeletedFalse(tagId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그입니다."));

        if (!tag.getOwner().getId().equals(user.getId())) {
            throw new PermissionDeniedException("본인 소유의 태그만 조회할 수 있습니다.");
        }

        return TagResponse.from(tag);
    }

    /**
     * 태그 삭제 (soft delete)
     */
    @Transactional
    public void deleteTag(Long tagId) {
        User user = UserContext.getCurrentUser();
        Tag tag = tagRepository.findByIdAndIsDeletedFalse(tagId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그입니다."));

        if (!tag.getOwner().getId().equals(user.getId())) {
            throw new PermissionDeniedException("본인 소유의 태그만 삭제할 수 있습니다.");
        }

        tag.setDeleted(true);
        tagRepository.save(tag);
    }

    /**
     * 품목에 태그 부착
     */
    @Transactional
    public void attachTagToItem(Long itemId, Long tagId) {
        User user = UserContext.getCurrentUser();
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));
        Tag tag = tagRepository.findByIdAndIsDeletedFalse(tagId)
                .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다."));

        if (!tag.getOwner().getId().equals(user.getId())) {
            throw new PermissionDeniedException("본인 소유의 태그만 부착할 수 있습니다.");
        }

        ItemTag itemTag = ItemTag.builder()
                .id(new ItemTagId(item.getId(), tag.getId()))
                .item(item)
                .tag(tag)
                .build();
        itemTagRepository.save(itemTag);

        tagActivityLogRepository.save(TagActivityLog.builder()
                .item(item)
                .tag(tag)
                .user(user)
                .action("ADD")
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * 품목에서 태그 제거
     */
    @Transactional
    public void detachTagFromItem(Long itemId, Long tagId) {
        User user = UserContext.getCurrentUser();
        ItemTagId id = new ItemTagId(itemId, tagId);
        ItemTag itemTag = itemTagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("태그가 부착되어 있지 않습니다."));

        itemTagRepository.delete(itemTag);

        tagActivityLogRepository.save(TagActivityLog.builder()
                .item(itemTag.getItem())
                .tag(itemTag.getTag())
                .user(user)
                .action("REMOVE")
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * 자동 태그 규칙 생성
     */
    @Transactional
    public void createTagRule(String condition, String tagName) {
        User user = UserContext.getCurrentUser();
        TagRule rule = TagRule.builder()
                .condition(condition)
                .tagName(tagName)
                .owner(user)
                .isDeleted(false)
                .build();
        tagRuleRepository.save(rule);
    }

    /**
     * 태그 작업 로그 전체 조회 (관리자 기준)
     */
    @Transactional(readOnly = true)
    public List<TagActivityLog> getTagActivityLogs() {
        User user = UserContext.getCurrentUser();
        return tagActivityLogRepository.findAllByUser(user);
    }
}
