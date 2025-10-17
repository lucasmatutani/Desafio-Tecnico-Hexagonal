package com.inventory.domain.policy;

import com.inventory.domain.model.Inventory;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationPolicy {
    
    private static final int DEFAULT_TTL_MINUTES = 15;
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY_PER_RESERVATION = 100;
    
    public ValidationResult validate(Inventory inventory, int quantity) {
        List<String> errors = new ArrayList<>();
        
        // Valida quantidade mínima
        if (quantity < MIN_QUANTITY) {
            errors.add(String.format(
                "Quantity must be at least %d", MIN_QUANTITY
            ));
        }
        
        // Valida quantidade máxima
        if (quantity > MAX_QUANTITY_PER_RESERVATION) {
            errors.add(String.format(
                "Quantity cannot exceed %d per reservation", 
                MAX_QUANTITY_PER_RESERVATION
            ));
        }
        
        // Valida estoque disponível
        if (!inventory.hasAvailableStock(quantity)) {
            errors.add(String.format(
                "Insufficient stock. Requested: %d, Available: %d",
                quantity, inventory.availableStock()
            ));
        }
        
        return errors.isEmpty() 
            ? ValidationResult.success() 
            : ValidationResult.failure(errors);
    }
    
    public Duration getTtl() {
        return Duration.ofMinutes(DEFAULT_TTL_MINUTES);
    }
    
    public boolean canReserve(Inventory inventory, int quantity) {
        return validate(inventory, quantity).isValid();
    }
    
    public int getMaxQuantityPerReservation() {
        return MAX_QUANTITY_PER_RESERVATION;
    }
}

