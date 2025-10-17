package com.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    private Long id;
    private StoreId storeId;
    private Sku sku;
    private String productName;
    private Stock stock;
    private LocalDateTime lastUpdated;
    
    public void reserve(int quantity) {
        this.stock = stock.reserve(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void commit(int quantity) {
        this.stock = stock.commit(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void release(int quantity) {
        this.stock = stock.release(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stock = new Stock(
            stock.availableStock() + quantity,
            stock.reservedStock(),
            stock.soldStock()
        );
        this.lastUpdated = LocalDateTime.now();
    }
    
    public boolean hasAvailableStock(int quantity) {
        return stock.hasAvailableStock(quantity);
    }
    
    public int availableStock() {
        return stock.availableStock();
    }
    
    public int reservedStock() {
        return stock.reservedStock();
    }
    
    public int soldStock() {
        return stock.soldStock();
    }
}

