package com.inventory.adapters.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to reserve stock for a customer")
public record ReserveStockRequest(
    
    @Schema(description = "Store identifier", example = "STORE-01", required = true)
    @NotBlank(message = "storeId is required")
    String storeId,
    
    @Schema(description = "Product SKU in format SKUxxx", example = "SKU123", required = true)
    @NotBlank(message = "sku is required")
    @Pattern(regexp = "^SKU\\d{3,6}$", message = "SKU must be in format SKUxxx")
    String sku,
    
    @Schema(description = "Quantity to reserve", example = "30", required = true, minimum = "1")
    @Min(value = 1, message = "quantity must be at least 1")
    int quantity,
    
    @Schema(description = "Customer identifier", example = "CUST-001", required = true)
    @NotBlank(message = "customerId is required")
    String customerId
) {
}

