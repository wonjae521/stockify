package com.stock.stockify.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private ReportType type;
    private Long warehouseId;
    private String periodStart;
    private String periodEnd;
}