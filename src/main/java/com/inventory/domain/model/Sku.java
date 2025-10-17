package com.inventory.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

public record Sku(String value) {
    
    private static final Pattern SKU_PATTERN = Pattern.compile("^SKU\\d{3,6}$");
    
    public Sku {
        Objects.requireNonNull(value, "SKU cannot be null");
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "SKU must be in format 'SKU' followed by 3-6 digits (e.g., SKU123)"
            );
        }
    }
    
    public static Sku of(String value) {
        return new Sku(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

