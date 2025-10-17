package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReserveStockRequest(
    
    @NotBlank(message = "storeId is required")
    String storeId,
    
    @NotBlank(message = "sku is required")
    @Pattern(regexp = "^SKU\\d{3,6}$", message = "SKU must be in format SKUxxx")
    String sku,
    
    @Min(value = 1, message = "quantity must be at least 1")
    int quantity,
    
    @NotBlank(message = "customerId is required")
    String customerId
) {
}

