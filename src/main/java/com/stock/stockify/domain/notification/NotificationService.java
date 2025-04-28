package com.stock.stockify.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 생성
    public void createNotification(String message) {
        Notification notification = Notification.builder()
                .message(message)
                .resolved(false)  // 🔥 read(false)가 아니라 resolved(false)
                .build();
        notificationRepository.save(notification);
    }

    // 모든 알림 조회
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // 알림 읽음 처리
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 알림이 존재하지 않습니다."));
        notification.setResolved(true); // 🔥 setRead가 아니라 setResolved
        notificationRepository.save(notification);
    }
}
