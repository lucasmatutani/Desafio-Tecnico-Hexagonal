package com.inventory.adapters.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Reservation operation response")
public record ReservationResponse(
    
    @Schema(description = "Unique reservation identifier", example = "RES-550e8400-e29b-41d4-a716-446655440000")
    String reservationId,
    
    @Schema(description = "Store identifier", example = "STORE-01")
    String storeId,
    
    @Schema(description = "Product SKU", example = "SKU123")
    String sku,
    
    @Schema(description = "Reserved quantity", example = "30")
    int quantity,
    
    @Schema(description = "Reservation status", example = "RESERVED", allowableValues = {"RESERVED", "COMMITTED", "CANCELLED", "EXPIRED"})
    String status,
    
    @Schema(description = "Expiration timestamp (15 minutes from creation)", example = "2025-10-18T18:00:00")
    LocalDateTime expiresAt,
    
    @Schema(description = "Human-readable message", example = "Stock reserved successfully. Expires at 2025-10-18T18:00:00")
    String message
) {
}

