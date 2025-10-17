package com.inventory.domain.model;

import java.util.Objects;
import java.util.UUID;

public record ReservationId(String value) {
    
    public ReservationId {
        Objects.requireNonNull(value, "ReservationId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ReservationId cannot be empty");
        }
    }
    
    public static ReservationId generate() {
        return new ReservationId("RES-" + UUID.randomUUID().toString());
    }
    
    public static ReservationId of(String value) {
        return new ReservationId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

