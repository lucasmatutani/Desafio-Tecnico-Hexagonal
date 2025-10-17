package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ReleaseStockRequest(
    
    @NotBlank(message = "reservationId is required")
    String reservationId,
    
    @NotBlank(message = "reason is required")
    String reason
) {
}

