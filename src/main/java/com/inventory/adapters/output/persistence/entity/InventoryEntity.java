package com.inventory.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"store_id", "sku"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "store_id", nullable = false, length = 50)
    private String storeId;
    
    @Column(name = "sku", nullable = false, length = 20)
    private String sku;
    
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;
    
    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock;
    
    @Column(name = "sold_stock", nullable = false)
    private Integer soldStock;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    
    @Version
    private Long version; // Optimistic locking support
}

