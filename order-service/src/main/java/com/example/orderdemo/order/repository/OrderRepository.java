package com.example.orderdemo.order.repository;

import com.example.orderdemo.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Order persistence.
 *
 * @author jiaolin
 */
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
