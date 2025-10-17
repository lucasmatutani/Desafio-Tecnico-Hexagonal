package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockAddedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String reason
) implements DomainEvent {
    
    public StockAddedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
    
    public static StockAddedEvent create(
            StoreId storeId,
            Sku sku,
            int quantity,
            String reason) {
        return new StockAddedEvent(
            UUID.randomUUID().toString(),
            "StockAdded",
            LocalDateTime.now(),
            sku.value(),
            storeId,
            sku,
            quantity,
            reason
        );
    }
}

