package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockCommittedEvent(
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
    
    public StockCommittedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
    }
    
    public static StockCommittedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String customerId) {
        return new StockCommittedEvent(
            UUID.randomUUID().toString(),
            "StockCommitted",
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

