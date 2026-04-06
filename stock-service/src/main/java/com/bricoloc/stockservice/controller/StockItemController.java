package com.bricoloc.stockservice.controller;

import com.bricoloc.stockservice.model.StockItem;
import com.bricoloc.stockservice.repository.StockItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin("*")
public class StockItemController {

    private final StockItemRepository repository;

    public StockItemController(StockItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StockItem> getAllStocks(
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public StockItem getStockItem(
            @PathVariable Long id,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public StockItem createStockItem(
            @RequestBody StockItem stockItem,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        stockItem.setTenantId(tenantId);   // Le tenant est forcé depuis le header
        return repository.save(stockItem);
    }
}