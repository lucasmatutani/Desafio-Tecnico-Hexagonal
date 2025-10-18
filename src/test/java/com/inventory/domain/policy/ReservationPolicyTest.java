package com.inventory.domain.policy;

import com.inventory.domain.model.Inventory;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.Stock;
import com.inventory.domain.model.StoreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ReservationPolicyTest {
    
    private ReservationPolicy policy;
    
    @BeforeEach
    void setUp() {
        policy = new ReservationPolicy();
    }
    
    @Test
    void shouldAcceptValidReservation() {
        // Given
        Inventory inventory = createInventory(100);
        
        // When
        ValidationResult result = policy.validate(inventory, 50);
        
        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.errors()).isEmpty();
    }
    
    @Test
    void shouldRejectZeroQuantity() {
        // Given
        Inventory inventory = createInventory(100);
        
        // When
        ValidationResult result = policy.validate(inventory, 0);
        
        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.errors()).contains("Quantity must be at least 1");
    }
    
    @Test
    void shouldRejectNegativeQuantity() {
        // Given
        Inventory inventory = createInventory(100);
        
        // When
        ValidationResult result = policy.validate(inventory, -5);
        
        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.errors()).contains("Quantity must be at least 1");
    }
    
    @Test
    void shouldRejectQuantityExceedingMaximum() {
        // Given
        Inventory inventory = createInventory(200);
        
        // When
        ValidationResult result = policy.validate(inventory, 101);
        
        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.errors())
            .anyMatch(error -> error.contains("cannot exceed 100"));
    }
    
    @Test
    void shouldRejectInsufficientStock() {
        // Given
        Inventory inventory = createInventory(10);
        
        // When
        ValidationResult result = policy.validate(inventory, 20);
        
        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.errors())
            .anyMatch(error -> error.contains("Insufficient stock"));
    }
    
    @Test
    void shouldReturnTtlOf15Minutes() {
        // When
        var ttl = policy.getTtl();
        
        // Then
        assertThat(ttl.toMinutes()).isEqualTo(15);
    }
    
    @Test
    void shouldReturnMaxQuantityPerReservation() {
        // When
        int max = policy.getMaxQuantityPerReservation();
        
        // Then
        assertThat(max).isEqualTo(100);
    }
    
    private Inventory createInventory(int availableStock) {
        return Inventory.builder()
            .id(1L)
            .storeId(StoreId.of("STORE-01"))
            .sku(Sku.of("SKU123"))
            .productName("Test Product")
            .stock(Stock.withAvailable(availableStock))
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}

