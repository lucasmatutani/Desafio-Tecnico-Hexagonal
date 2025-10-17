package com.inventory.domain.model;

import java.util.Objects;

public record StoreId(String value) {
    
    public StoreId {
        Objects.requireNonNull(value, "StoreId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("StoreId cannot be empty");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("StoreId too long (max 50 chars)");
        }
    }
    
    public static StoreId of(String value) {
        return new StoreId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

