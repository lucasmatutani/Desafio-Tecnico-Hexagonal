package com.inventory.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class InventoryTest {
    
    @Test
    void shouldReserveStock() {
        // Given
        Inventory inventory = createInventory(100, 0, 0);
        
        // When
        inventory.reserve(10);
        
        // Then
        assertThat(inventory.availableStock()).isEqualTo(90);
        assertThat(inventory.reservedStock()).isEqualTo(10);
        assertThat(inventory.soldStock()).isZero();
        assertThat(inventory.getLastUpdated()).isNotNull();
    }
    
    @Test
    void shouldCommitReservedStock() {
        // Given
        Inventory inventory = createInventory(90, 10, 0);
        
        // When
        inventory.commit(10);
        
        // Then
        assertThat(inventory.availableStock()).isEqualTo(90);
        assertThat(inventory.reservedStock()).isZero();
        assertThat(inventory.soldStock()).isEqualTo(10);
    }
    
    @Test
    void shouldReleaseReservedStock() {
        // Given
        Inventory inventory = createInventory(90, 10, 0);
        
        // When
        inventory.release(10);
        
        // Then
        assertThat(inventory.availableStock()).isEqualTo(100);
        assertThat(inventory.reservedStock()).isZero();
        assertThat(inventory.soldStock()).isZero();
    }
    
    @Test
    void shouldAddStock() {
        // Given
        Inventory inventory = createInventory(100, 0, 0);
        
        // When
        inventory.addStock(50);
        
        // Then
        assertThat(inventory.availableStock()).isEqualTo(150);
    }
    
    @Test
    void shouldThrowWhenAddingNegativeStock() {
        // Given
        Inventory inventory = createInventory(100, 0, 0);
        
        // When/Then
        assertThatThrownBy(() -> inventory.addStock(-10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Quantity must be positive");
    }
    
    @Test
    void shouldUpdateLastUpdatedOnEveryOperation() {
        // Given
        Inventory inventory = createInventory(100, 0, 0);
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        
        // When
        inventory.reserve(10);
        
        // Then
        assertThat(inventory.getLastUpdated()).isAfter(before);
    }
    
    @Test
    void shouldCheckAvailableStock() {
        // Given
        Inventory inventory = createInventory(50, 10, 5);
        
        // Then
        assertThat(inventory.hasAvailableStock(30)).isTrue();
        assertThat(inventory.hasAvailableStock(50)).isTrue();
        assertThat(inventory.hasAvailableStock(51)).isFalse();
    }
    
    private Inventory createInventory(int available, int reserved, int sold) {
        return Inventory.builder()
            .id(1L)
            .storeId(StoreId.of("STORE-01"))
            .sku(Sku.of("SKU123"))
            .productName("Test Product")
            .stock(new Stock(available, reserved, sold))
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}

