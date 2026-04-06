package com.bricoloc.tenant.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    @Column(name = "tenant_id", nullable = false, unique = true)
    private String tenantId;

    /** Domaine HTTP du partenaire — ex: leroy.localhost:8080 */
    @Column(nullable = false, unique = true)
    private String domain;

    /** Nom affiché dans le front */
    @Column(nullable = false)
    private String name;

    /** URL du logo */
    private String logoUrl;

    /** Couleur principale en hex — ex: #e63946 */
    @Column(nullable = false)
    private String primaryColor;

    /** Couleur secondaire en hex */
    private String secondaryColor;

    // --- Feature flags ---
    @Column(nullable = false)
    private boolean enableDelivery = true;

    @Column(nullable = false)
    private boolean enableDeposit = true;

    @Column(nullable = false)
    private boolean enableOnlinePayment = true;

    /** Langue par défaut : fr, en, de... */
    @Column(nullable = false)
    private String defaultLanguage = "fr";

    // --- Constructeur vide obligatoire pour JPA ---
    public Tenant() {}

    // --- Getters / Setters ---
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }
    public boolean isEnableDelivery() { return enableDelivery; }
    public void setEnableDelivery(boolean enableDelivery) { this.enableDelivery = enableDelivery; }
    public boolean isEnableDeposit() { return enableDeposit; }
    public void setEnableDeposit(boolean enableDeposit) { this.enableDeposit = enableDeposit; }
    public boolean isEnableOnlinePayment() { return enableOnlinePayment; }
    public void setEnableOnlinePayment(boolean v) { this.enableOnlinePayment = v; }
    public String getDefaultLanguage() { return defaultLanguage; }
    public void setDefaultLanguage(String defaultLanguage) { this.defaultLanguage = defaultLanguage; }
}