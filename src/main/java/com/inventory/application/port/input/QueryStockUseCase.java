package com.inventory.application.port.input;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Optional;

public interface QueryStockUseCase {
    Optional<InventoryView> findByStoreAndSku(StoreId storeId, Sku sku);
}

