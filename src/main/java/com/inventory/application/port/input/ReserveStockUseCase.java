package com.inventory.application.port.input;

import com.inventory.domain.model.ReservationId;

public interface ReserveStockUseCase {
    Result<ReservationId, DomainError> reserve(ReserveStockCommand command);
}

