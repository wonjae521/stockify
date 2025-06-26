package com.stock.stockify.domain.report;

public enum ReportType {
    WEEKLY_REPORT, // 주간 보고서
    MONTHLY_REPORT, // 월간 보고서
    SALES_PREDICTION, // 판매량 예측
    LOW_STOCK_ALERT, // 품절될 가능성이 높은 품목 예측
    INVENTORY_TURNOVER, // 재고 회전율
    OUT_OF_STOCK_FORECAST, // 품목별 예상 품절일 예측
    ALERT_RECOMMENDATION // 주의가 필요한 상품
}