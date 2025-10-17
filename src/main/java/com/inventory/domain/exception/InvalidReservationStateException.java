package com.inventory.domain.exception;

import com.inventory.domain.model.ReservationStatus;
import java.util.Map;

public class InvalidReservationStateException extends DomainException {
    
    public InvalidReservationStateException(
            String reservationId,
            ReservationStatus currentState,
            ReservationStatus expectedState) {
        super(
            "INVALID_RESERVATION_STATE",
            String.format(
                "Invalid state for reservation %s. Current: %s, Expected: %s",
                reservationId, currentState, expectedState
            ),
            Map.of(
                "reservationId", reservationId,
                "currentState", currentState.toString(),
                "expectedState", expectedState.toString()
            )
        );
    }
}

