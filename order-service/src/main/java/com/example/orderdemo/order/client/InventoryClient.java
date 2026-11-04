package com.example.orderdemo.order.client;

import com.example.orderdemo.order.client.dto.DeductRequest;
import com.example.orderdemo.order.client.dto.DeductResponse;
import com.example.orderdemo.order.client.dto.InventoryView;
import com.example.orderdemo.order.client.dto.StockView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * OpenFeign client for inventory-service (service-to-service).
 *
 * @author jiaolin
 */
@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryClient {

    /**
     * Query inventory details.
     *
     * @param skuId SKU identifier
     * @return inventory view
     */
    @GetMapping("/api/inventory/{skuId}")
    InventoryView getInventory(@PathVariable("skuId") String skuId);

    /**
     * Query stock.
     *
     * @param skuId SKU identifier
     * @return stock view
     */
    @GetMapping("/api/inventory/{skuId}/stock")
    StockView getStock(@PathVariable("skuId") String skuId);

    /**
     * Deduct stock remotely.
     *
     * @param skuId SKU identifier
     * @param request deduct body
     * @return deduct result
     */
    @PostMapping("/api/inventory/{skuId}/deduct")
    DeductResponse deduct(@PathVariable("skuId") String skuId, @RequestBody DeductRequest request);
}
