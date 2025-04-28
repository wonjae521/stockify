package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final ItemTagRepository itemTagRepository;
    private final TagRuleRepository tagRuleRepository;
    private final TagActivityLogRepository tagActivityLogRepository;
    private final InventoryItemRepository inventoryItemRepository;

    // 태그 생성
    public Tag createTag(String name, String description, Tag.Type type) {
        Tag tag = Tag.builder()
                .name(name)
                .description(description)
                .type(type)
                .build();
        return tagRepository.save(tag);
    }

    // 태그 전체 조회
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // 품목에 태그 부착
    public void addTagToItem(Long itemId, Long tagId) {
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
                .build();
        tagActivityLogRepository.save(log);
    }

    // 품목에 태그 제거
    public void removeTagFromItem(Long itemId, Long tagId) {
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
                .build();
        tagActivityLogRepository.save(log);
    }

    // 자동 태그 부착 규칙 생성
    public TagRule createTagRule(Long tagId, TagRule.ConditionType conditionType, String conditionValue) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다."));

        TagRule rule = TagRule.builder()
                .tag(tag)
                .conditionType(conditionType)
                .conditionValue(conditionValue)
                .enabled(true)
                .build();
        return tagRuleRepository.save(rule);
    }

    // 태그 부착/제거 이력 조회
    public List<TagActivityLog> getTagActivityLogs() {
        return tagActivityLogRepository.findAll();
    }
}
