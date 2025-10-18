package com.inventory.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StockTest {
    
    @Test
    void shouldCreateEmptyStock() {
        // When
        Stock stock = Stock.empty();
        
        // Then
        assertThat(stock.availableStock()).isZero();
        assertThat(stock.reservedStock()).isZero();
        assertThat(stock.soldStock()).isZero();
        assertThat(stock.totalStock()).isZero();
    }
    
    @Test
    void shouldCreateStockWithAvailable() {
        // When
        Stock stock = Stock.withAvailable(100);
        
        // Then
        assertThat(stock.availableStock()).isEqualTo(100);
        assertThat(stock.reservedStock()).isZero();
        assertThat(stock.soldStock()).isZero();
        assertThat(stock.totalStock()).isEqualTo(100);
    }
    
    @Test
    void shouldReserveStock() {
        // Given
        Stock stock = Stock.withAvailable(100);
        
        // When
        Stock reserved = stock.reserve(10);
        
        // Then
        assertThat(reserved.availableStock()).isEqualTo(90);
        assertThat(reserved.reservedStock()).isEqualTo(10);
        assertThat(reserved.soldStock()).isZero();
        assertThat(reserved.totalStock()).isEqualTo(100);
    }
    
    @Test
    void shouldThrowWhenReservingMoreThanAvailable() {
        // Given
        Stock stock = Stock.withAvailable(10);
        
        // When/Then
        assertThatThrownBy(() -> stock.reserve(20))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot reserve 20 units. Only 10 available");
    }
    
    @Test
    void shouldCommitReservedStock() {
        // Given
        Stock stock = new Stock(90, 10, 0);
        
        // When
        Stock committed = stock.commit(10);
        
        // Then
        assertThat(committed.availableStock()).isEqualTo(90);
        assertThat(committed.reservedStock()).isZero();
        assertThat(committed.soldStock()).isEqualTo(10);
    }
    
    @Test
    void shouldThrowWhenCommittingMoreThanReserved() {
        // Given
        Stock stock = new Stock(90, 10, 0);
        
        // When/Then
        assertThatThrownBy(() -> stock.commit(20))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot commit 20 units. Only 10 reserved");
    }
    
    @Test
    void shouldReleaseReservedStock() {
        // Given
        Stock stock = new Stock(90, 10, 0);
        
        // When
        Stock released = stock.release(5);
        
        // Then
        assertThat(released.availableStock()).isEqualTo(95);
        assertThat(released.reservedStock()).isEqualTo(5);
        assertThat(released.soldStock()).isZero();
    }
    
    @Test
    void shouldCheckIfHasAvailableStock() {
        // Given
        Stock stock = Stock.withAvailable(50);
        
        // Then
        assertThat(stock.hasAvailableStock(30)).isTrue();
        assertThat(stock.hasAvailableStock(50)).isTrue();
        assertThat(stock.hasAvailableStock(51)).isFalse();
    }
    
    @Test
    void shouldRejectNegativeValues() {
        // When/Then
        assertThatThrownBy(() -> new Stock(-1, 0, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Available stock cannot be negative");
        
        assertThatThrownBy(() -> new Stock(0, -1, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Reserved stock cannot be negative");
        
        assertThatThrownBy(() -> new Stock(0, 0, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Sold stock cannot be negative");
    }
    
    @Test
    void shouldBeImmutable() {
        // Given
        Stock original = Stock.withAvailable(100);
        
        // When
        Stock reserved = original.reserve(10);
        
        // Then - original não foi modificado
        assertThat(original.availableStock()).isEqualTo(100);
        assertThat(original.reservedStock()).isZero();
        
        // E o novo objeto tem os valores corretos
        assertThat(reserved.availableStock()).isEqualTo(90);
        assertThat(reserved.reservedStock()).isEqualTo(10);
    }
}

