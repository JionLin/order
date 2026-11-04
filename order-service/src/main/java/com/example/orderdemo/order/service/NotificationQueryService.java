package com.example.orderdemo.order.service;

import com.example.orderdemo.order.dto.NotificationResponse;
import com.example.orderdemo.order.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read-side for demo notifications.
 *
 * @author jiaolin
 */
@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * List all notifications newest-first by id.
     *
     * @return notification list
     */
    @Transactional(readOnly = true)
    public List<NotificationResponse> listNotifications() {
        return notificationRepository.findAll().stream()
                .sorted((left, right) -> Long.compare(right.getId(), left.getId()))
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }
}
