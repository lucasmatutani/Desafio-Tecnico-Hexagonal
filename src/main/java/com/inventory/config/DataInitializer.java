package com.inventory.config;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import com.inventory.adapters.output.persistence.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final InventoryJpaRepository inventoryRepository;
    
    @Override
    public void run(String... args) {
        log.info("🚀 Initializing database with sample data...");
        
        if (inventoryRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }
        
        List<InventoryEntity> initialInventory = List.of(
            createInventory("STORE-01", "SKU123", "Notebook Dell XPS 13", 100),
            createInventory("STORE-01", "SKU456", "iPhone 15 Pro", 50),
            createInventory("STORE-01", "SKU789", "Samsung Galaxy S24", 75),
            createInventory("STORE-02", "SKU123", "Notebook Dell XPS 13", 80),
            createInventory("STORE-02", "SKU456", "iPhone 15 Pro", 40),
            createInventory("STORE-03", "SKU789", "Samsung Galaxy S24", 60)
        );
        
        inventoryRepository.saveAll(initialInventory);
        
        log.info("✅ Database initialized with {} products", initialInventory.size());
        log.info("Sample SKUs: SKU123, SKU456, SKU789");
        log.info("Sample Stores: STORE-01, STORE-02, STORE-03");
    }
    
    private InventoryEntity createInventory(
            String storeId, 
            String sku, 
            String productName, 
            int stock) {
        return InventoryEntity.builder()
            .storeId(storeId)
            .sku(sku)
            .productName(productName)
            .availableStock(stock)
            .reservedStock(0)
            .soldStock(0)
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}

