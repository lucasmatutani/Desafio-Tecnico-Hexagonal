package com.inventory.adapters.input.rest.dto;

public record InventoryResponse(
    String storeId,
    String sku,
    String productName,
    int availableStock,
    int reservedStock,
    int soldStock,
    int totalStock
) {
}

