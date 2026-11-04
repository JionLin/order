package com.example.orderdemo.inventory.web;

import com.example.orderdemo.inventory.dto.DeductRequest;
import com.example.orderdemo.inventory.dto.DeductResponse;
import com.example.orderdemo.inventory.dto.InventoryResponse;
import com.example.orderdemo.inventory.dto.StockResponse;
import com.example.orderdemo.inventory.service.InventoryAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory REST surface for query and deduct.
 *
 * @author jiaolin
 */
@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory")
public class InventoryController {

    private final InventoryAppService inventoryAppService;

    public InventoryController(InventoryAppService inventoryAppService) {
        this.inventoryAppService = inventoryAppService;
    }

    /**
     * Query SKU inventory details.
     *
     * @param skuId SKU identifier
     * @return inventory response
     */
    @GetMapping("/{skuId}")
    @Operation(summary = "Get inventory by SKU")
    public InventoryResponse getInventory(@PathVariable String skuId) {
        return inventoryAppService.getInventory(skuId);
    }

    /**
     * Query stock only (Redis preferred).
     *
     * @param skuId SKU identifier
     * @return stock response
     */
    @GetMapping("/{skuId}/stock")
    @Operation(summary = "Get stock by SKU (Redis cache preferred)")
    public StockResponse getStock(@PathVariable String skuId) {
        return inventoryAppService.getStock(skuId);
    }

    /**
     * Deduct stock (used by order-service via OpenFeign; also exposed for demo).
     *
     * @param skuId SKU identifier
     * @param request deduct body
     * @return deduct result
     */
    @PostMapping("/{skuId}/deduct")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deduct stock atomically (Redis Lua + MySQL)")
    public DeductResponse deduct(@PathVariable String skuId, @Valid @RequestBody DeductRequest request) {
        return inventoryAppService.deduct(skuId, request.getQuantity());
    }
}
