package com.stock.stockify.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AIReportRepository extends JpaRepository<AIReport, Long> {
    // 기본 CRUD (Create, Read, Update, Delete) 제공
}
