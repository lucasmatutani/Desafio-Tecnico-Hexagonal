package com.inventory.application.port.output;

import com.inventory.domain.model.Inventory;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Optional;

public interface InventoryRepository {
    Optional<Inventory> findByStoreIdAndSku(StoreId storeId, Sku sku);
    Optional<Inventory> findByStoreIdAndSkuWithLock(StoreId storeId, Sku sku);
    Inventory save(Inventory inventory);
    boolean existsByStoreIdAndSku(StoreId storeId, Sku sku);
}

