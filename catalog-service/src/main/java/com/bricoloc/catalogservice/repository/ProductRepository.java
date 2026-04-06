package com.bricoloc.catalogservice.repository;

import com.bricoloc.catalogservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTenantId(String tenantId);
    Optional<Product> findByIdAndTenantId(Long id, String tenantId);
}