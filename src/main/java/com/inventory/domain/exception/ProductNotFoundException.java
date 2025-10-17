package com.inventory.domain.exception;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Map;

public class ProductNotFoundException extends DomainException {
    
    public ProductNotFoundException(Sku sku, StoreId storeId) {
        super(
            "PRODUCT_NOT_FOUND",
            String.format("Product %s not found in store %s", sku.value(), storeId.value()),
            Map.of(
                "sku", sku.value(),
                "storeId", storeId.value()
            )
        );
    }
}

