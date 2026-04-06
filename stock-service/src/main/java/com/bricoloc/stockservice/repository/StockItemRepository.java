package com.bricoloc.stockservice.repository;

import com.bricoloc.stockservice.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByTenantId(String tenantId);
    Optional<StockItem> findByIdAndTenantId(Long id, String tenantId);
}