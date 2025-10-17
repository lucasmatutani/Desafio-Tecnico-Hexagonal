package com.inventory.domain.policy;

import com.inventory.domain.model.Stock;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockValidationPolicy {
    
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int CRITICAL_STOCK_THRESHOLD = 5;
    private static final int MAX_STOCK_PER_ITEM = 10000;
    
    public ValidationResult validateStockOperation(
            Stock currentStock, 
            int quantity, 
            StockOperation operation) {
        
        List<String> errors = new ArrayList<>();
        
        switch (operation) {
            case ADD -> validateAdd(currentStock, quantity, errors);
            case RESERVE -> validateReserve(currentStock, quantity, errors);
            case COMMIT -> validateCommit(currentStock, quantity, errors);
            case RELEASE -> validateRelease(currentStock, quantity, errors);
        }
        
        return errors.isEmpty() 
            ? ValidationResult.success() 
            : ValidationResult.failure(errors);
    }
    
    private void validateAdd(Stock stock, int quantity, List<String> errors) {
        if (quantity <= 0) {
            errors.add("Quantity to add must be positive");
        }
        
        int newTotal = stock.totalStock() + quantity;
        if (newTotal > MAX_STOCK_PER_ITEM) {
            errors.add(String.format(
                "Cannot add %d units. Would exceed maximum stock of %d",
                quantity, MAX_STOCK_PER_ITEM
            ));
        }
    }
    
    private void validateReserve(Stock stock, int quantity, List<String> errors) {
        if (quantity <= 0) {
            errors.add("Quantity to reserve must be positive");
        }
        
        if (quantity > stock.availableStock()) {
            errors.add(String.format(
                "Cannot reserve %d units. Only %d available",
                quantity, stock.availableStock()
            ));
        }
    }
    
    private void validateCommit(Stock stock, int quantity, List<String> errors) {
        if (quantity > stock.reservedStock()) {
            errors.add(String.format(
                "Cannot commit %d units. Only %d reserved",
                quantity, stock.reservedStock()
            ));
        }
    }
    
    private void validateRelease(Stock stock, int quantity, List<String> errors) {
        if (quantity > stock.reservedStock()) {
            errors.add(String.format(
                "Cannot release %d units. Only %d reserved",
                quantity, stock.reservedStock()
            ));
        }
    }
    
    public boolean isLowStock(Stock stock) {
        return stock.availableStock() < LOW_STOCK_THRESHOLD;
    }
    
    public boolean isCriticalStock(Stock stock) {
        return stock.availableStock() < CRITICAL_STOCK_THRESHOLD;
    }
    
    public StockLevel checkStockLevel(Stock stock) {
        if (stock.availableStock() == 0) {
            return StockLevel.OUT_OF_STOCK;
        } else if (isCriticalStock(stock)) {
            return StockLevel.CRITICAL;
        } else if (isLowStock(stock)) {
            return StockLevel.LOW;
        } else {
            return StockLevel.NORMAL;
        }
    }
}

