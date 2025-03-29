package com.pragma.order_service.repository;

import com.pragma.order_service.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity,Long> {
}
