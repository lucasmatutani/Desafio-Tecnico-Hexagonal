package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CommitStockRequest(
    
    @NotBlank(message = "reservationId is required")
    String reservationId,
    
    @NotBlank(message = "orderId is required")
    String orderId
) {
}

