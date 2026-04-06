package com.bricoloc.catalogservice.controller;

import com.bricoloc.catalogservice.model.Product;
import com.bricoloc.catalogservice.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin("*")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Product> getAllProducts(
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Product createProduct(
            @RequestBody Product product,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        product.setTenantId(tenantId);   // Le tenant est forcé depuis le header
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "bricoloc") String tenantId) {
        Product product = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.delete(product);
    }
}