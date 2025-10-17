package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.mapper.InventoryPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.InventoryJpaRepository;
import com.inventory.application.port.output.InventoryRepository;
import com.inventory.domain.model.Inventory;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryJpaAdapter implements InventoryRepository {
    
    private final InventoryJpaRepository jpaRepository;
    private final InventoryPersistenceMapper mapper;
    
    @Override
    public Optional<Inventory> findByStoreIdAndSku(StoreId storeId, Sku sku) {
        log.debug("Finding inventory - store: {}, sku: {}", storeId, sku);
        
        return jpaRepository
            .findByStoreIdAndSku(storeId.value(), sku.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Inventory> findByStoreIdAndSkuWithLock(StoreId storeId, Sku sku) {
        log.debug("Finding inventory with lock - store: {}, sku: {}", storeId, sku);
        
        return jpaRepository
            .findByStoreIdAndSkuWithLock(storeId.value(), sku.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public Inventory save(Inventory inventory) {
        log.debug("Saving inventory - sku: {}, available: {}", 
            inventory.getSku(), inventory.availableStock());
        
        var entity = mapper.toEntity(inventory);
        var saved = jpaRepository.save(entity);
        
        return mapper.toDomain(saved);
    }
    
    @Override
    public boolean existsByStoreIdAndSku(StoreId storeId, Sku sku) {
        return jpaRepository.existsByStoreIdAndSku(storeId.value(), sku.value());
    }
}

