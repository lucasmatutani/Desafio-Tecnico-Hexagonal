package com.inventory.adapters.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Inventory information for a product")
public record InventoryResponse(
    
    @Schema(description = "Store identifier", example = "STORE-01")
    String storeId,
    
    @Schema(description = "Product SKU", example = "SKU123")
    String sku,
    
    @Schema(description = "Product name", example = "Notebook Dell XPS 13")
    String productName,
    
    @Schema(description = "Available stock (can be reserved)", example = "70")
    int availableStock,
    
    @Schema(description = "Reserved stock (waiting for confirmation)", example = "30")
    int reservedStock,
    
    @Schema(description = "Sold stock (confirmed sales)", example = "0")
    int soldStock,
    
    @Schema(description = "Total stock (available + reserved)", example = "100")
    int totalStock
) {
}

