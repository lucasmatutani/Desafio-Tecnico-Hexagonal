package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    
    Optional<InventoryEntity> findByStoreIdAndSku(String storeId, String sku);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryEntity i WHERE i.storeId = :storeId AND i.sku = :sku")
    Optional<InventoryEntity> findByStoreIdAndSkuWithLock(
        @Param("storeId") String storeId, 
        @Param("sku") String sku
    );
    
    boolean existsByStoreIdAndSku(String storeId, String sku);
}

