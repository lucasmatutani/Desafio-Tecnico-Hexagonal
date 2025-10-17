package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockReservedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    String reservationId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String customerId
) implements DomainEvent {
    
    public StockReservedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
    
    public static StockReservedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String customerId) {
        return new StockReservedEvent(
            UUID.randomUUID().toString(),
            "StockReserved",
            LocalDateTime.now(),
            sku.value(),
            reservationId,
            storeId,
            sku,
            quantity,
            customerId
        );
    }
}

