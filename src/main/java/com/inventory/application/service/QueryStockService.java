package com.inventory.application.service;

import com.inventory.application.port.input.InventoryView;
import com.inventory.application.port.input.QueryStockUseCase;
import com.inventory.application.port.output.InventoryRepository;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryStockService implements QueryStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryView> findByStoreAndSku(StoreId storeId, Sku sku) {
        
        log.debug("Querying stock - store: {}, sku: {}", storeId, sku);
        
        return inventoryRepository
            .findByStoreIdAndSku(storeId, sku)
            .map(inventory -> {
                log.debug("Inventory found - available: {}", 
                    inventory.availableStock());
                return InventoryView.from(inventory);
            });
    }
}

