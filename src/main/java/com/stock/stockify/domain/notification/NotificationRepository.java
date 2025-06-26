package com.stock.stockify.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

// Notification 엔티티에 대한 JPA 레포지토리
// 기본적인 CRUD 메서드는 JpaRepository가 자동으로 제공해준다.
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 기본적인 save, findById, findAll, deleteById 등을 JpaRepository가 자동 제공
}
