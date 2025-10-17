package com.inventory.application.port.input;

import com.inventory.domain.exception.DomainException;
import java.util.Map;

public record DomainError(
    String code,
    String message,
    Map<String, Object> details
) {
    public static DomainError from(DomainException ex) {
        return new DomainError(
            ex.getCode(),
            ex.getMessage(),
            ex.getDetails()
        );
    }
    
    public static DomainError of(String code, String message) {
        return new DomainError(code, message, Map.of());
    }
}

