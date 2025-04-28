package com.stock.stockify.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 기본적인 CRUD 메서드는 JpaRepository가 자동으로 제공해준다.
}
