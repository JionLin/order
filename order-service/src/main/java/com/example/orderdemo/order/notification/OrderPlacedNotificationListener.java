package com.example.orderdemo.order.notification;

import com.example.orderdemo.order.domain.NotificationEntity;
import com.example.orderdemo.order.event.OrderPlacedEvent;
import com.example.orderdemo.order.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * In-process async-style notification handler (no external MQ).
 *
 * @author jiaolin
 */
@Component
public class OrderPlacedNotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPlacedNotificationListener.class);

    private final NotificationRepository notificationRepository;

    public OrderPlacedNotificationListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Persist a notification row and log the side effect.
     *
     * @param event order placed event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderPlaced(OrderPlacedEvent event) {
        String message = "Order " + event.getOrderId() + " placed for sku "
                + event.getSkuId() + " x" + event.getQuantity();
        NotificationEntity entity = new NotificationEntity(event.getOrderId(), event.getSkuId(), message);
        notificationRepository.save(entity);
        LOGGER.info("Notification stored: {}", message);
    }
}
