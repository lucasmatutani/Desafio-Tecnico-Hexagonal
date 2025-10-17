package com.inventory.application.port.input;

import com.inventory.domain.model.Inventory;

public record InventoryView(
    String storeId,
    String sku,
    String productName,
    int availableStock,
    int reservedStock,
    int soldStock
) {
    public static InventoryView from(Inventory inventory) {
        return new InventoryView(
            inventory.getStoreId().value(),
            inventory.getSku().value(),
            inventory.getProductName(),
            inventory.availableStock(),
            inventory.reservedStock(),
            inventory.soldStock()
        );
    }
}

