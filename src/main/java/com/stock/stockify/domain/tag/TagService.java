package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.auth.PermissionChecker;
import com.stock.stockify.global.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 태그와 관련된 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final ItemTagRepository itemTagRepository;
    private final TagRuleRepository tagRuleRepository;
    private final TagActivityLogRepository tagActivityLogRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PermissionChecker permissionChecker;

    /** 태그 생성 */
    public Tag createTag(String name, String description, Tag.Type type) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_MANAGE");
        User owner = user.getAdminOwner();

        Tag tag = Tag.builder()
                .name(name)
                .description(description)
                .type(type)
                .owner(owner)
                .build();

        return tagRepository.save(tag);
    }

    /** ADMIN 소유 태그 전체 조회 */
    public List<Tag> getAllTags() {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_VIEW");
        User owner = user.getAdminOwner();
        return tagRepository.findByOwner(owner);
    }

    /** 품목에 태그 부착 */
    public void addTagToItem(Long itemId, Long tagId) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_MANAGE");

        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 품목입니다."));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다."));

        ItemTag itemTag = ItemTag.builder()
                .id(new ItemTagId(itemId, tagId))
                .inventoryItem(item)
                .tag(tag)
                .build();

        itemTagRepository.save(itemTag);

        // 부착 활동 기록
        TagActivityLog log = TagActivityLog.builder()
                .inventoryItem(item)
                .tag(tag)
                .action(TagActivityLog.Action.ADDED)
                .performedBy(user)
                .build();
        tagActivityLogRepository.save(log);
    }

    /** 품목에 태그 제거 */
    public void removeTagFromItem(Long itemId, Long tagId) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_MANAGE");
        ItemTagId id = new ItemTagId(itemId, tagId);
        itemTagRepository.deleteById(id);

        // 제거 활동 기록
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 품목입니다."));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다."));

        TagActivityLog log = TagActivityLog.builder()
                .inventoryItem(item)
                .tag(tag)
                .action(TagActivityLog.Action.REMOVED)
                .performedBy(user)
                .build();
        tagActivityLogRepository.save(log);
    }

    /** 태그 자동 부착 규칙 생성 */
    public TagRule createTagRule(Long tagId, TagRule.ConditionType conditionType, String conditionValue) {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_MANAGE");
        User owner = user.getAdminOwner();

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다."));

        TagRule rule = TagRule.builder()
                .tag(tag)
                .conditionType(conditionType)
                .conditionValue(conditionValue)
                .enabled(true)
                .owner(owner)
                .build();

        return tagRuleRepository.save(rule);
    }

    /** 태그 작업 로그 전체 조회 (ADMIN 기준) */
    public List<TagActivityLog> getTagActivityLogs() {
        User user = UserContext.getCurrentUser();
        permissionChecker.check(user.getId(), "TAG_MANAGE");
        User owner = user.getAdminOwner();
        return tagActivityLogRepository.findAll().stream()
                .filter(log -> log.getInventoryItem().getOwner().equals(owner))
                .toList();
    }
}
