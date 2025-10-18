package com.inventory.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ReservationTest {
    
    @Test
    void shouldCreateReservation() {
        // When
        Reservation reservation = createReservation(ReservationStatus.RESERVED);
        
        // Then
        assertThat(reservation.getId()).isNotBlank();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(reservation.getQuantity()).isEqualTo(10);
    }
    
    @Test
    void shouldDetectExpiredReservation() {
        // Given
        Reservation reservation = Reservation.builder()
            .id("RES-001")
            .storeId(StoreId.of("STORE-01"))
            .sku(Sku.of("SKU123"))
            .quantity(10)
            .customerId("CUST-001")
            .status(ReservationStatus.RESERVED)
            .createdAt(LocalDateTime.now().minusMinutes(20))
            .expiresAt(LocalDateTime.now().minusMinutes(5))
            .build();
        
        // Then
        assertThat(reservation.isExpired()).isTrue();
    }
    
    @Test
    void shouldDetectNotExpiredReservation() {
        // Given
        Reservation reservation = Reservation.builder()
            .id("RES-001")
            .storeId(StoreId.of("STORE-01"))
            .sku(Sku.of("SKU123"))
            .quantity(10)
            .customerId("CUST-001")
            .status(ReservationStatus.RESERVED)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .build();
        
        // Then
        assertThat(reservation.isExpired()).isFalse();
    }
    
    @Test
    void shouldBeCommittableWhenReservedAndNotExpired() {
        // Given
        Reservation reservation = createReservation(ReservationStatus.RESERVED);
        
        // Then
        assertThat(reservation.canBeCommitted()).isTrue();
    }
    
    @Test
    void shouldNotBeCommittableWhenAlreadyCommitted() {
        // Given
        Reservation reservation = createReservation(ReservationStatus.COMMITTED);
        
        // Then
        assertThat(reservation.canBeCommitted()).isFalse();
    }
    
    @Test
    void shouldChangeStatus() {
        // Given
        Reservation reservation = createReservation(ReservationStatus.RESERVED);
        
        // When
        Reservation committed = reservation.withStatus(ReservationStatus.COMMITTED);
        
        // Then
        assertThat(committed.getStatus()).isEqualTo(ReservationStatus.COMMITTED);
        assertThat(committed.getCommittedAt()).isNotNull();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED); // Original não mudou
    }
    
    private Reservation createReservation(ReservationStatus status) {
        return Reservation.builder()
            .id("RES-001")
            .storeId(StoreId.of("STORE-01"))
            .sku(Sku.of("SKU123"))
            .quantity(10)
            .customerId("CUST-001")
            .status(status)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .build();
    }
}

