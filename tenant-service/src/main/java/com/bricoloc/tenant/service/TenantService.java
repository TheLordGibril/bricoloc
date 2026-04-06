package com.bricoloc.tenant.service;

import com.bricoloc.tenant.entity.Tenant;
import com.bricoloc.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository repository;

    public TenantService(TenantRepository repository) {
        this.repository = repository;
    }

    public List<Tenant> findAll() {
        return repository.findAll();
    }

    public Optional<Tenant> findById(String tenantId) {
        return repository.findById(tenantId);
    }

    public Optional<Tenant> findByDomain(String domain) {
        return repository.findByDomain(domain);
    }

    public Tenant create(Tenant tenant) {
        if (repository.existsById(tenant.getTenantId())) {
            throw new IllegalArgumentException(
                    "Tenant '" + tenant.getTenantId() + "' existe déjà."
            );
        }
        return repository.save(tenant);
    }

    public void delete(String tenantId) {
        repository.deleteById(tenantId);
    }

    public Tenant update(String tenantId, Tenant updatedTenant) {
        Tenant existing = repository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant introuvable"));

        existing.setName(updatedTenant.getName());
        existing.setDomain(updatedTenant.getDomain());
        existing.setLogoUrl(updatedTenant.getLogoUrl());
        existing.setPrimaryColor(updatedTenant.getPrimaryColor());
        existing.setSecondaryColor(updatedTenant.getSecondaryColor());
        existing.setEnableDelivery(updatedTenant.isEnableDelivery());
        existing.setEnableDeposit(updatedTenant.isEnableDeposit());
        existing.setEnableOnlinePayment(updatedTenant.isEnableOnlinePayment());
        existing.setDefaultLanguage(updatedTenant.getDefaultLanguage());

        return repository.save(existing);
    }
}