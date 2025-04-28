package com.stock.stockify.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // ✅ 1. 태그 생성
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagRequest request) {
        Tag tag = tagService.createTag(request.getName(), request.getDescription(), request.getType());
        return ResponseEntity.ok(tag);
    }

    // ✅ 2. 모든 태그 조회
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    // ✅ 3. 품목에 태그 부착
    @PostMapping("/{itemId}/add")
    public ResponseEntity<String> addTagToItem(@PathVariable Long itemId, @RequestParam Long tagId) {
        tagService.addTagToItem(itemId, tagId);
        return ResponseEntity.ok("태그 부착 완료");
    }

    // ✅ 4. 품목에서 태그 제거
    @PostMapping("/{itemId}/remove")
    public ResponseEntity<String> removeTagFromItem(@PathVariable Long itemId, @RequestParam Long tagId) {
        tagService.removeTagFromItem(itemId, tagId);
        return ResponseEntity.ok("태그 제거 완료");
    }

    // ✅ 5. 자동 태그 부착 규칙 생성
    @PostMapping("/rules")
    public ResponseEntity<TagRule> createTagRule(@RequestBody TagRuleRequest request) {
        TagRule rule = tagService.createTagRule(request.getTagId(), request.getConditionType(), request.getConditionValue());
        return ResponseEntity.ok(rule);
    }

    // ✅ 6. 태그 부착/제거 활동 로그 조회
    @GetMapping("/activities")
    public ResponseEntity<List<TagActivityLog>> getTagActivityLogs() {
        List<TagActivityLog> logs = tagService.getTagActivityLogs();
        return ResponseEntity.ok(logs);
    }
}
