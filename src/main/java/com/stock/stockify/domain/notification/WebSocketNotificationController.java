package com.stock.stockify.domain.notification;

import com.stock.stockify.domain.notification.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationMessage sendNotification(NotificationMessage message) {
        System.out.println("💬 WebSocket 메시지 수신: " + message.getTitle() + " / " + message.getContent());
        return message;
    }
}
