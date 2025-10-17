package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockReleasedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    String reservationId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String reason
) implements DomainEvent {
    
    public StockReleasedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
    }
    
    public static StockReleasedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String reason) {
        return new StockReleasedEvent(
            UUID.randomUUID().toString(),
            "StockReleased",
            LocalDateTime.now(),
            sku.value(),
            reservationId,
            storeId,
            sku,
            quantity,
            reason
        );
    }
}

