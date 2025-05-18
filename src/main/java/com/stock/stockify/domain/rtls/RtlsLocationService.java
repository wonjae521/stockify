package com.stock.stockify.domain.rtls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 재고,사용자 위치 저장 및 조회 / 비즈니스 로직 분리
@Service
@RequiredArgsConstructor
public class RtlsLocationService {

    private final RtlsItemLocationRepository itemRepo;
    private final RtlsUserLocationRepository userRepo;

    public List<RtlsItemLocation> getItemLocationHistory(Long itemId) {
        return itemRepo.findByItemIdOrderByDetectedAtDesc(itemId);
    }

    public List<RtlsUserLocation> getUserLocationHistory(Long userId) {
        return userRepo.findByUserIdOrderByDetectedAtDesc(userId);
    }

    public void saveItemLocation(RtlsItemLocation location) {
        itemRepo.save(location);
    }

    public void saveUserLocation(RtlsUserLocation location) {
        userRepo.save(location);
    }
}
