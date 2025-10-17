package com.inventory.application.port.input;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Objects;

public record ReserveStockCommand(
    StoreId storeId,
    Sku sku,
    int quantity,
    String customerId
) {
    public ReserveStockCommand {
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        Objects.requireNonNull(customerId, "customerId cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}

