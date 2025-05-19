package com.stock.stockify.domain.report;

public interface ReportDataService {
    String generateJsonStats(Long warehouseId, String periodStart, String periodEnd);
}
