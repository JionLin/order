package com.example.orderdemo.order.service;

import com.example.orderdemo.order.client.InventoryClient;
import com.example.orderdemo.order.client.dto.DeductRequest;
import com.example.orderdemo.order.domain.OrderEntity;
import com.example.orderdemo.order.domain.OrderStatus;
import com.example.orderdemo.order.dto.CreateOrderRequest;
import com.example.orderdemo.order.dto.OrderResponse;
import com.example.orderdemo.order.event.OrderPlacedEvent;
import com.example.orderdemo.order.exception.OrderNotFoundException;
import com.example.orderdemo.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Place and query orders; deducts inventory via OpenFeign then publishes OrderPlacedEvent.
 *
 * @author jiaolin
 */
@Service
public class OrderAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAppService.class);

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final ApplicationEventPublisher eventPublisher;

    public OrderAppService(OrderRepository orderRepository,
                           InventoryClient inventoryClient,
                           ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create an order after a successful inventory deduct call.
     *
     * @param request create request
     * @return created order
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        inventoryClient.deduct(request.getSkuId(), new DeductRequest(request.getQuantity()));

        OrderEntity saved = orderRepository.save(
                new OrderEntity(request.getSkuId(), request.getQuantity(), OrderStatus.CREATED));

        eventPublisher.publishEvent(new OrderPlacedEvent(this, saved.getId(), saved.getSkuId(), saved.getQuantity()));
        LOGGER.info("Order created id={}, skuId={}, quantity={}", saved.getId(), saved.getSkuId(), saved.getQuantity());
        return OrderResponse.from(saved);
    }

    /**
     * Get one order by id.
     *
     * @param orderId order id
     * @return order response
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return OrderResponse.from(entity);
    }

    /**
     * List all orders.
     *
     * @return order list
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> listOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
