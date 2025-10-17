package com.inventory.domain.exception;

import java.util.Map;

public class InvalidStockOperationException extends DomainException {
    
    public InvalidStockOperationException(String operation, String reason) {
        super(
            "INVALID_STOCK_OPERATION",
            String.format("Invalid stock operation: %s. Reason: %s", operation, reason),
            Map.of(
                "operation", operation,
                "reason", reason
            )
        );
    }
}

