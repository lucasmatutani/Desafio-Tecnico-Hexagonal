package com.inventory.domain.exception;

import java.util.Map;

public class ReservationNotFoundException extends DomainException {
    
    public ReservationNotFoundException(String reservationId) {
        super(
            "RESERVATION_NOT_FOUND",
            String.format("Reservation not found: %s", reservationId),
            Map.of("reservationId", reservationId)
        );
    }
}

