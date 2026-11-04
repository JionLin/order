package com.example.orderdemo.order.web;

import com.example.orderdemo.order.dto.NotificationResponse;
import com.example.orderdemo.order.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo notification list endpoint.
 *
 * @author jiaolin
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationQueryService notificationQueryService;

    public NotificationController(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * List notifications produced by OrderPlacedEvent listeners.
     *
     * @return notification list
     */
    @GetMapping
    @Operation(summary = "List in-process notifications")
    public List<NotificationResponse> list() {
        return notificationQueryService.listNotifications();
    }
}
