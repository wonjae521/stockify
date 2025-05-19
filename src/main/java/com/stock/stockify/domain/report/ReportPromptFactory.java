package com.stock.stockify.domain.report;

public class ReportPromptFactory {
    public static String generatePrompt(ReportType type, String jsonData, String start, String end) {
        switch (type) {
            case WEEKLY_REPORT:
                return "다음은 " + start + "부터 " + end + "까지의 주간 재고 및 주문 기록입니다. 이를 분석해서 요약 보고서를 작성해줘." + "\n\n" + jsonData;
            case MONTHLY_REPORT:
                return "다음은 " + start + "부터 " + end + "까지의 월간 재고 및 판매 데이터입니다. 전체 흐름을 분석해줘." + "\n\n" + jsonData;
            case SALES_PREDICTION:
                return "다음 데이터는 최근 재고 및 판매 기록입니다. 향후 한 달간 예상 매출을 예측해줘." + "\n\n" + jsonData;
            case LOW_STOCK_ALERT:
                return "곧 품절될 가능성이 높은 품목을 아래 데이터를 기반으로 분석해줘." + "\n\n" + jsonData;
            case INVENTORY_TURNOVER:
                return "다음 입출고 데이터를 기반으로 회전율 높은 품목과 낮은 품목을 구분해줘." + "\n\n" + jsonData;
            case OUT_OF_STOCK_FORECAST:
                return "최근 출고 속도와 현재 재고 수량을 고려하여 품목별 예상 품절일을 예측해줘." + "\n\n" + jsonData;
            case ALERT_RECOMMENDATION:
                return "다음 데이터를 분석해서 관리자에게 필요한 경고 메시지를 작성해줘." + "\n\n" + jsonData;
            default:
                return "지원되지 않는 보고서 유형입니다.";
        }
    }
}