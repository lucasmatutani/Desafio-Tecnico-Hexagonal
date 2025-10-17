package com.inventory.domain.model;

public record Stock(
    int availableStock,
    int reservedStock,
    int soldStock
) {
    
    public Stock {
        if (availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be negative");
        }
        if (reservedStock < 0) {
            throw new IllegalArgumentException("Reserved stock cannot be negative");
        }
        if (soldStock < 0) {
            throw new IllegalArgumentException("Sold stock cannot be negative");
        }
    }
    
    public static Stock empty() {
        return new Stock(0, 0, 0);
    }
    
    public static Stock withAvailable(int quantity) {
        return new Stock(quantity, 0, 0);
    }
    
    public Stock reserve(int quantity) {
        if (quantity > availableStock) {
            throw new IllegalArgumentException(
                String.format("Cannot reserve %d units. Only %d available", 
                    quantity, availableStock)
            );
        }
        return new Stock(
            availableStock - quantity,
            reservedStock + quantity,
            soldStock
        );
    }
    
    public Stock commit(int quantity) {
        if (quantity > reservedStock) {
            throw new IllegalArgumentException(
                String.format("Cannot commit %d units. Only %d reserved", 
                    quantity, reservedStock)
            );
        }
        return new Stock(
            availableStock,
            reservedStock - quantity,
            soldStock + quantity
        );
    }
    
    public Stock release(int quantity) {
        if (quantity > reservedStock) {
            throw new IllegalArgumentException(
                String.format("Cannot release %d units. Only %d reserved", 
                    quantity, reservedStock)
            );
        }
        return new Stock(
            availableStock + quantity,
            reservedStock - quantity,
            soldStock
        );
    }
    
    public int totalStock() {
        return availableStock + reservedStock;
    }
    
    public boolean hasAvailableStock(int quantity) {
        return availableStock >= quantity;
    }
}

