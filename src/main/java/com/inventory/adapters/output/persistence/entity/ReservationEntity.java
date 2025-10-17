package com.inventory.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_expires_at", columnList = "expires_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(name = "store_id", nullable = false, length = 50)
    private String storeId;
    
    @Column(name = "sku", nullable = false, length = 20)
    private String sku;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;
    
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReservationStatusEntity status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "committed_at")
    private LocalDateTime committedAt;
}

