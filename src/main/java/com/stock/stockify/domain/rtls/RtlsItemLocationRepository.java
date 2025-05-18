package com.stock.stockify.domain.rtls;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// DB 접근
public interface RtlsItemLocationRepository extends JpaRepository<RtlsItemLocation, Long> {
    List<RtlsItemLocation> findByItemIdOrderByDetectedAtDesc(Long itemId);
}
