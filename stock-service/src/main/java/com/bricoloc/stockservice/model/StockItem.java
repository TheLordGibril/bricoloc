package com.bricoloc.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "stocks")
public class StockItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;  // Lien vers products.id
    private int quantity;
    private String location;
}
