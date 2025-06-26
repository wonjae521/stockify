package com.stock.stockify.domain.rtls;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// DB 접근
public interface RtlsUserLocationRepository extends JpaRepository<RtlsUserLocation, Long> {
    List<RtlsUserLocation> findByUserIdOrderByDetectedAtDesc(Long userId);
}
