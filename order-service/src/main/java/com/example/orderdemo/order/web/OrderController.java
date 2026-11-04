package com.example.orderdemo.order.web;

import com.example.orderdemo.order.dto.CreateOrderRequest;
import com.example.orderdemo.order.dto.OrderResponse;
import com.example.orderdemo.order.service.OrderAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order REST surface.
 *
 * @author jiaolin
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders")
public class OrderController {

    private final OrderAppService orderAppService;

    public OrderController(OrderAppService orderAppService) {
        this.orderAppService = orderAppService;
    }

    /**
     * Place an order (deduct via OpenFeign, then notify via ApplicationEvent).
     *
     * @param request create body
     * @return created order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order (OpenFeign deduct + ApplicationEvent notify)")
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderAppService.createOrder(request);
    }

    /**
     * Get order by id.
     *
     * @param id order id
     * @return order
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    public OrderResponse get(@PathVariable Long id) {
        return orderAppService.getOrder(id);
    }

    /**
     * List orders.
     *
     * @return order list
     */
    @GetMapping
    @Operation(summary = "List orders")
    public List<OrderResponse> list() {
        return orderAppService.listOrders();
    }
}
