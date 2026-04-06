#!/usr/bin/env python3
"""
BricoLoc — CLI de provisioning marque blanche
Usage : python3 provision.py --id <id> --name <nom> --domain <domaine> [options]
"""

import argparse
import json
import sys
import urllib.request
import urllib.error

GATEWAY = "http://localhost:8080"

# Catalogue de démo injecté pour chaque nouveau tenant
DEMO_PRODUCTS = [
    {"name": "Perceuse visseuse sans fil", "description": "18V, livré avec 2 batteries", "price": 49.90, "category": "Outillage électroportatif"},
    {"name": "Bétonneuse 100L",            "description": "Moteur 550W, tambour basculant", "price": 35.00, "category": "Gros outillage"},
    {"name": "Échafaudage roulant",         "description": "Hauteur 3m, aluminium", "price": 28.00, "category": "Accès en hauteur"},
    {"name": "Ponceuse orbitale",           "description": "125mm, 300W", "price": 15.00, "category": "Outillage électroportatif"},
    {"name": "Groupe électrogène 3kW",      "description": "Démarrage électrique, AVR", "price": 65.00, "category": "Énergie"},
]

def request(method, path, data=None, headers=None):
    url = GATEWAY + path
    body = json.dumps(data).encode() if data else None
    h = {"Content-Type": "application/json"}
    if headers:
        h.update(headers)
    req = urllib.request.Request(url, data=body, headers=h, method=method)
    try:
        with urllib.request.urlopen(req) as resp:
            return json.loads(resp.read().decode())
    except urllib.error.HTTPError as e:
        body = e.read().decode()
        print(f"  ✗ HTTP {e.code} sur {method} {path} : {body}")
        sys.exit(1)
    except urllib.error.URLError as e:
        print(f"  ✗ Impossible de joindre le gateway sur {GATEWAY} : {e.reason}")
        print("    → Vérifie que docker compose est démarré.")
        sys.exit(1)

def create_tenant(args):
    payload = {
        "tenantId":           args.id,
        "name":               args.name,
        "domain":             args.domain,
        "primaryColor":       args.primary,
        "secondaryColor":     args.secondary,
        "logoUrl":            args.logo,
        "enableDelivery":     args.delivery,
        "enableDeposit":      args.deposit,
        "enableOnlinePayment": args.payment,
        "defaultLanguage":    args.lang,
    }
    print(f"\n[1/3] Création du tenant '{args.id}'...")
    tenant = request("POST", "/api/admin/tenants", payload)
    print(f"  ✓ Tenant créé : {tenant['name']} ({tenant['tenantId']})")
    return tenant

def seed_catalog(tenant_id):
    print(f"\n[2/3] Injection du catalogue de démo ({len(DEMO_PRODUCTS)} produits)...")
    headers = {"X-Tenant-Id": tenant_id}
    created = []
    for p in DEMO_PRODUCTS:
        product = request("POST", "/api/catalog", p, headers)
        print(f"  ✓ Produit #{product['id']} — {product['name']}")
        created.append(product)
    return created

def seed_stock(tenant_id, products):
    print(f"\n[3/3] Initialisation du stock...")
    headers = {"X-Tenant-Id": tenant_id}
    for product in products:
        stock = {
            "productId": product["id"],
            "quantity":  10,
            "location":  "Dépôt principal",
        }
        item = request("POST", "/api/stock", stock, headers)
        print(f"  ✓ Stock #{item['id']} — produit {item['productId']} : {item['quantity']} unités")

def print_summary(args):
    print(f"""
╔══════════════════════════════════════════════════════╗
║           Site partenaire créé avec succès !         ║
╠══════════════════════════════════════════════════════╣
║  ID         : {args.id:<38}║
║  Nom        : {args.name:<38}║
║  Domaine    : {args.domain:<38}║
║  Couleur    : {args.primary:<38}║
║  Langue     : {args.lang:<38}║
╠══════════════════════════════════════════════════════╣
║  Prochaine étape :                                   ║
║  Ajoute dans /etc/hosts :                            ║
║  127.0.0.1   {args.domain.split(':')[0]:<38}║
║  Puis ouvre : http://{args.domain:<31}║
╚══════════════════════════════════════════════════════╝
""")

def main():
    global GATEWAY
    parser = argparse.ArgumentParser(
        description="BricoLoc — Provisioning marque blanche",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Exemples :
  python3 provision.py --id castotools --name "CastoTools" --domain "castotools.localhost:3000"
  python3 provision.py --id probat2 --name "ProBat Sud" --domain "probat2.localhost:3000" --primary "#e63946" --no-deposit
        """
    )

    # Obligatoires
    parser.add_argument("--id",     required=True, help="Identifiant unique du tenant (ex: castotools)")
    parser.add_argument("--name",   required=True, help="Nom affiché du site (ex: CastoTools)")
    parser.add_argument("--domain", required=True, help="Domaine local (ex: castotools.localhost:3000)")

    # Optionnels
    parser.add_argument("--primary",   default="#01696f", help="Couleur primaire hex (défaut: #01696f)")
    parser.add_argument("--secondary", default="#0c4e54", help="Couleur secondaire hex (défaut: #0c4e54)")
    parser.add_argument("--logo",      default="",        help="URL du logo")
    parser.add_argument("--lang",      default="fr",      choices=["fr","en","de","es"], help="Langue par défaut")
    parser.add_argument("--gateway",   default=GATEWAY,   help=f"URL du gateway (défaut: {GATEWAY})")

    # Feature flags
    parser.add_argument("--no-delivery", dest="delivery", action="store_false", help="Désactiver la livraison")
    parser.add_argument("--no-deposit",  dest="deposit",  action="store_false", help="Désactiver la caution")
    parser.add_argument("--no-payment",  dest="payment",  action="store_false", help="Désactiver le paiement en ligne")
    parser.set_defaults(delivery=True, deposit=True, payment=True)

    args = parser.parse_args()

    # Surcharge gateway si passé en argument
    GATEWAY = args.gateway

    print("╔══════════════════════════════════════════════════════╗")
    print("║     BricoLoc — Provisioning marque blanche          ║")
    print("╚══════════════════════════════════════════════════════╝")

    create_tenant(args)
    products = seed_catalog(args.id)
    seed_stock(args.id, products)
    print_summary(args)

if __name__ == "__main__":
    main()