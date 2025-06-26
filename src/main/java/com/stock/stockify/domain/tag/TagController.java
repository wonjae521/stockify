package com.stock.stockify.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 태그 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /** 태그 생성 API */
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagRequest request) {
        Tag tag = tagService.createTag(request.getName(), request.getDescription(), request.getType());
        return ResponseEntity.ok(tag);
    }

    /** 전체 태그 조회 (ADMIN 기준) */
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /** 품목에 태그 부착 */
    @PostMapping("/{itemId}/add")
    public ResponseEntity<String> addTagToItem(@PathVariable Long itemId, @RequestParam Long tagId) {
        tagService.addTagToItem(itemId, tagId);
        return ResponseEntity.ok("태그 부착 완료");
    }

    /** 품목에서 태그 제거 */
    @PostMapping("/{itemId}/remove")
    public ResponseEntity<String> removeTagFromItem(@PathVariable Long itemId, @RequestParam Long tagId) {
        tagService.removeTagFromItem(itemId, tagId);
        return ResponseEntity.ok("태그 제거 완료");
    }

    /** 자동 태그 규칙 생성 */
    @PostMapping("/rules")
    public ResponseEntity<TagRule> createTagRule(@RequestBody TagRuleRequest request) {
        TagRule rule = tagService.createTagRule(request.getTagId(), request.getConditionType(), request.getConditionValue());
        return ResponseEntity.ok(rule);
    }

    /** 태그 부착/해제 활동 로그 전체 조회 */
    @GetMapping("/activities")
    public ResponseEntity<List<TagActivityLog>> getTagActivityLogs() {
        List<TagActivityLog> logs = tagService.getTagActivityLogs();
        return ResponseEntity.ok(logs);
    }
}
