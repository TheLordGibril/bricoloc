package com.bricoloc.tenant.repository;

import com.bricoloc.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    /**
     * Résout le tenant depuis le header Host HTTP.
     * Appelé par l'API Gateway pour injecter X-Tenant-Id.
     * Ex: findByDomain("leroy.localhost:8080") → Tenant{tenantId="leroy"}
     */
    Optional<Tenant> findByDomain(String domain);
}