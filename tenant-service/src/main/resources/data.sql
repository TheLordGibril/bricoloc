INSERT INTO tenant (tenant_id, domain, name, logo_url,
                    primary_color, secondary_color,
                    enable_delivery, enable_deposit, enable_online_payment, default_language)
VALUES ('bricoloc', 'localhost:3000', 'BricoLoc',
        'https://cdn.simpleicons.org/hammer',
        '#01696f', '#0c4e54', true, true, true, 'fr')
    ON CONFLICT (tenant_id) DO NOTHING;

INSERT INTO tenant (tenant_id, domain, name, logo_url,
                    primary_color, secondary_color,
                    enable_delivery, enable_deposit, enable_online_payment, default_language)
VALUES ('leroy', 'leroy.localhost:3000', 'LeRoy Location',
        'https://cdn.simpleicons.org/toolbox',
        '#e63946', '#c1121f', true, false, true, 'fr')
    ON CONFLICT (tenant_id) DO NOTHING;

INSERT INTO tenant (tenant_id, domain, name, logo_url,
                    primary_color, secondary_color,
                    enable_delivery, enable_deposit, enable_online_payment, default_language)
VALUES ('probat', 'probat.localhost:3000', 'ProBat Outillage',
        'https://cdn.simpleicons.org/build',
        '#f4a261', '#e76f51', false, false, false, 'fr')
    ON CONFLICT (tenant_id) DO NOTHING;