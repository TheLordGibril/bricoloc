package com.bricoloc.tenant.controller;

import com.bricoloc.tenant.entity.Tenant;
import com.bricoloc.tenant.service.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")  // Autorise le front HTML à appeler directement (POC)
public class TenantController {

    private final TenantService service;

    public TenantController(TenantService service) {
        this.service = service;
    }

    // ── Endpoint public : résolution par domaine ──
    // Utilisé par l'API Gateway pour injecter X-Tenant-Id
    // GET /tenants/by-domain?host=leroy.localhost:8080
    @GetMapping("/tenants/by-domain")
    public ResponseEntity<Tenant> getByDomain(@RequestParam String host) {
        return service.findByDomain(host)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Endpoints admin ──

    // GET /admin/tenants → liste tous les tenants
    @GetMapping("/admin/tenants")
    public List<Tenant> getAll() {
        return service.findAll();
    }

    // GET /admin/tenants/{id} → détail d'un tenant
    @GetMapping("/admin/tenants/{tenantId}")
    public ResponseEntity<Tenant> getById(@PathVariable String tenantId) {
        return service.findById(tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tenants/current")
    public ResponseEntity<Tenant> getCurrentTenant(
            @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId) {
        if (tenantId == null) return ResponseEntity.notFound().build();
        return service.findById(tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /admin/tenants → création one-click d'un nouveau partenaire
    @PostMapping("/admin/tenants")
    public ResponseEntity<Tenant> create(@RequestBody Tenant tenant) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(tenant));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // DELETE /admin/tenants/{id} → suppression d'un tenant
    @DeleteMapping("/admin/tenants/{tenantId}")
    public ResponseEntity<Void> delete(@PathVariable String tenantId) {
        service.delete(tenantId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/tenants/{tenantId}")
    public ResponseEntity<Tenant> update(@PathVariable("tenantId") String tenantId,
                                         @RequestBody Tenant tenant) {
        try {
            Tenant updated = service.update(tenantId, tenant);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}