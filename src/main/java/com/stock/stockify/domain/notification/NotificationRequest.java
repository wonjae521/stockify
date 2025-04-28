package com.stock.stockify.domain.notification;

import lombok.Getter;
import lombok.Setter;

// 알림 생성 요청을 받을 때 사용하는 DTO
@Getter
@Setter
public class NotificationRequest {
    private String message; // 알림 메시지
}
