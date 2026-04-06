package com.bricoloc.api_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtre global exécuté sur TOUTES les requêtes passant par l'API Gateway.
 *
 * Fonctionnement :
 *   1. Lit le header "Host" de la requête (ex: leroy.localhost:8080)
 *   2. Appelle tenant-service → GET /tenants/by-domain?host=leroy.localhost:8080
 *   3. Récupère le tenantId (ex: "leroy")
 *   4. Injecte le header "X-Tenant-Id: leroy" dans la requête forwardée
 *
 * Résultat : catalog-service et stock-service reçoivent X-Tenant-Id
 * et peuvent filtrer leurs données par tenant.
 */
@Component
public class TenantResolutionFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    // Valeur lue depuis application.yml : tenant.service.url
    // Docker  → http://tenant-service:8083
    // Local   → http://localhost:8083
    public TenantResolutionFilter(
            @Value("${tenant.service.url}") String tenantServiceUrl) {
        this.webClient = WebClient.create(tenantServiceUrl);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Ne pas interférer avec les routes admin
        if (path.startsWith("/api/admin")) {
            return chain.filter(exchange);
        }

        // Si X-Tenant-Id est déjà présent (ex: appel direct curl), on le respecte
        String existingTenant = exchange.getRequest().getHeaders().getFirst("X-Tenant-Id");
        if (existingTenant != null && !existingTenant.isBlank()) {
            return chain.filter(exchange);
        }

        // Sinon on résout depuis le domaine (cas front navigateur)
        String host = exchange.getRequest().getHeaders().getFirst("Host");

        if (host == null || host.isBlank()) {
            return chain.filter(withTenant(exchange, "bricoloc"));
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tenants/by-domain")
                        .queryParam("host", host)
                        .build())
                .retrieve()
                .bodyToMono(TenantConfig.class)
                .flatMap(tenant -> chain.filter(withTenant(exchange, tenant.tenantId())))
                .onErrorResume(e -> {
                    System.err.println("[TenantFilter] Fallback bricoloc — " + e.getMessage());
                    return chain.filter(withTenant(exchange, "bricoloc"));
                });
    }

    private ServerWebExchange withTenant(ServerWebExchange exchange,
                                         String tenantId) {
        return exchange.mutate()
                .request(r -> r.header("X-Tenant-Id", tenantId))
                .build();
    }

    @Override
    public int getOrder() {
        return -1; // Priorité maximale : s'exécute avant tous les autres filtres
    }

    // DTO interne : seul tenantId est nécessaire ici
    record TenantConfig(
            String tenantId, String domain, String name,
            String primaryColor, String secondaryColor,
            boolean enableDelivery, boolean enableDeposit,
            boolean enableOnlinePayment, String defaultLanguage
    ) {}
}