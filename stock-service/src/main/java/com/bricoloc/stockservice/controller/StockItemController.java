package com.bricoloc.stockservice.controller;

import com.bricoloc.stockservice.model.StockItem;
import com.bricoloc.stockservice.repository.StockItemRepository;
import org.springframework.web.bind.annotation.*;
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
    public List<StockItem> getAllStocks() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public StockItem getStockItem(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public StockItem createStockItem(@RequestBody StockItem StockItem) {
        return repository.save(StockItem);
    }
}
