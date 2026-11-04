package com.example.orderdemo.order.repository;

import com.example.orderdemo.order.domain.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Notification persistence.
 *
 * @author jiaolin
 */
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
