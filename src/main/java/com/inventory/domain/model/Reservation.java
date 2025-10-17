package com.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    private String id;
    private StoreId storeId;
    private Sku sku;
    private int quantity;
    private String customerId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime committedAt;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean canBeCommitted() {
        return status == ReservationStatus.RESERVED && !isExpired();
    }
    
    public boolean canBeReleased() {
        return status == ReservationStatus.RESERVED;
    }
    
    public Reservation withStatus(ReservationStatus newStatus) {
        return Reservation.builder()
            .id(this.id)
            .storeId(this.storeId)
            .sku(this.sku)
            .quantity(this.quantity)
            .customerId(this.customerId)
            .status(newStatus)
            .createdAt(this.createdAt)
            .expiresAt(this.expiresAt)
            .committedAt(newStatus == ReservationStatus.COMMITTED 
                ? LocalDateTime.now() 
                : this.committedAt)
            .build();
    }
}

