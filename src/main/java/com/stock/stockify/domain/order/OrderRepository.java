package com.stock.stockify.domain.order;

import com.stock.stockify.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderRepository extends JpaRepository<Order, Long> {
}
