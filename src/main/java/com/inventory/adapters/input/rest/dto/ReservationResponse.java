package com.inventory.adapters.input.rest.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
    String reservationId,
    String storeId,
    String sku,
    int quantity,
    String status,
    LocalDateTime expiresAt,
    String message
) {
}

