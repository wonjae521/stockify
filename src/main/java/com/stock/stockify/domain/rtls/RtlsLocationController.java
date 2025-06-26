package com.stock.stockify.domain.rtls;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rtls")
@RequiredArgsConstructor
public class RtlsLocationController {

    private final RtlsLocationService rtlsLocationService;

    // 특정 재고의 최근 위치 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<RtlsItemLocation> getLatestItemLocation(@PathVariable Long itemId) {
        List<RtlsItemLocation> locations = rtlsLocationService.getItemLocationHistory(itemId);
        return locations.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(locations.get(0));
    }

    // 특정 재고의 위치 이력 조회
    @GetMapping("/item/{itemId}/history")
    public ResponseEntity<List<RtlsItemLocation>> getItemLocationHistory(@PathVariable Long itemId) {
        return ResponseEntity.ok(rtlsLocationService.getItemLocationHistory(itemId));
    }

    // 특정 사용자의 최근 위치 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<RtlsUserLocation> getLatestUserLocation(@PathVariable Long userId) {
        List<RtlsUserLocation> locations = rtlsLocationService.getUserLocationHistory(userId);
        return locations.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(locations.get(0));
    }

    // 특정 사용자의 위치 이력 조회
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<RtlsUserLocation>> getUserLocationHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(rtlsLocationService.getUserLocationHistory(userId));
    }

    // 재고 위치 기록 저장
    @PostMapping("/item-location")
    public ResponseEntity<Void> saveItemLocation(@RequestBody RtlsItemLocation location) {
        rtlsLocationService.saveItemLocation(location);
        return ResponseEntity.ok().build();
    }

    // 사용자 위치 기록 저장
    @PostMapping("/user-location")
    public ResponseEntity<Void> saveUserLocation(@RequestBody RtlsUserLocation location) {
        rtlsLocationService.saveUserLocation(location);
        return ResponseEntity.ok().build();
    }
}
