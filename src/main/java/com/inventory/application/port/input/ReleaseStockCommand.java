package com.inventory.application.port.input;

import java.util.Objects;

public record ReleaseStockCommand(
    String reservationId,
    String reason
) {
    public ReleaseStockCommand {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");
    }
}

