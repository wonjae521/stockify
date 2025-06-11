package com.stock.stockify.domain.report;

import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.permission.PermissionRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserService;
import com.stock.stockify.global.auth.PermissionChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// AIReport 관련 API 요청을 처리하는 컨트롤러
// 리포트 생성, 조회, 삭제 기능 제공
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AIReportController {

    private final AIReportService aiReportService;
    private final PermissionChecker permissionChecker;
    private final UserService userService;
    private final PermissionRepository permissionRepository;

    // 리포트 생성
    @PostMapping
    public ResponseEntity<AIReport> createReport(@PathVariable Long warehouseId,
                                                 @RequestBody @Valid AIReportRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        // 권한 확인
        User user = userService.getUserFromUserDetails(userDetails);
        Permission permission = permissionRepository.findByName("REPORT_MANAGE")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        AIReport report = AIReport.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return ResponseEntity.ok(aiReportService.createReport(report));
    }

    // 모든 리포트 조회
    @GetMapping
    public ResponseEntity<List<AIReport>> getAllReports(@PathVariable Long warehouseId,
                                                        @AuthenticationPrincipal UserDetails userDetails) {

        // 권한 확인
        User user = userService.getUserFromUserDetails(userDetails);
        Permission permission = permissionRepository.findByName("REPORT_VIEW")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        return ResponseEntity.ok(aiReportService.getAllReports());
    }

    // 특정 ID로 리포트 조회
    @GetMapping("/{id}")
    public ResponseEntity<AIReport> getReportById(@PathVariable Long id, Long warehouseId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {

        // 권한 확인
        User user = userService.getUserFromUserDetails(userDetails);
        Permission permission = permissionRepository.findByName("REPORT_VIEW")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        return ResponseEntity.ok(aiReportService.getReportById(id));
    }

    // 특정 ID로 리포트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id, Long warehouseId,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        // 권한 확인
        User user = userService.getUserFromUserDetails(userDetails);
        Permission permission = permissionRepository.findByName("REPORT_MANAGE")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        aiReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
