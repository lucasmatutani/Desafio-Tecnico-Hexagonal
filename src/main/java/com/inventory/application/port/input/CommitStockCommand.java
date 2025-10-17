package com.inventory.application.port.input;

import java.util.Objects;

public record CommitStockCommand(
    String reservationId,
    String orderId
) {
    public CommitStockCommand {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(orderId, "orderId cannot be null");
    }
}

