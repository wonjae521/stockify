package com.stock.stockify.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Notification 관련 비즈니스 로직 처리 서비스 클래스
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 생성
    public void createNotification(String message) {
        Notification notification = Notification.builder()
                .message(message)
                .resolved(false) // 읽지 않음 상태
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
        notification.setResolved(true); // 읽음 상태로 변경
        notificationRepository.save(notification);
    }
}
