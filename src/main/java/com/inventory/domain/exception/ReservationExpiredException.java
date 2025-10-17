package com.inventory.domain.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ReservationExpiredException extends DomainException {
    
    private final String reservationId;
    private final LocalDateTime expiredAt;
    
    public ReservationExpiredException(
            String reservationId, 
            LocalDateTime expiredAt,
            int ttlMinutes) {
        super(
            "RESERVATION_EXPIRED",
            String.format("Reservation %s expired at %s", reservationId, expiredAt),
            Map.of(
                "reservationId", reservationId,
                "expiredAt", expiredAt.toString(),
                "ttlMinutes", ttlMinutes
            )
        );
        this.reservationId = reservationId;
        this.expiredAt = expiredAt;
    }
}

