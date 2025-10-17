package com.inventory.domain.exception;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.Getter;
import java.util.Map;

@Getter
public class InsufficientStockException extends DomainException {
    
    private final Sku sku;
    private final int requestedQuantity;
    private final int availableQuantity;
    private final StoreId storeId;
    
    public InsufficientStockException(
            Sku sku, 
            int requestedQuantity, 
            int availableQuantity,
            StoreId storeId) {
        super(
            "INSUFFICIENT_STOCK",
            String.format(
                "Insufficient stock for %s. Requested: %d, Available: %d",
                sku.value(), requestedQuantity, availableQuantity
            ),
            Map.of(
                "sku", sku.value(),
                "requested", requestedQuantity,
                "available", availableQuantity,
                "storeId", storeId.value()
            )
        );
        this.sku = sku;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
        this.storeId = storeId;
    }
}

