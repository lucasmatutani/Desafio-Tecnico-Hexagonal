package com.inventory.domain.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    
    private final String code;
    private final Map<String, Object> details;
    
    protected DomainException(String code, String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details != null ? details : Map.of();
    }
    
    protected DomainException(String code, String message) {
        this(code, message, Map.of());
    }
}

