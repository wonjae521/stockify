package com.stock.stockify.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;

// AIReport 엔티티에 대한 JPA 레포지토리
// 기본 CRUD (Create, Read, Update, Delete) 제공
public interface AIReportRepository extends JpaRepository<AIReport, Long> {

}
