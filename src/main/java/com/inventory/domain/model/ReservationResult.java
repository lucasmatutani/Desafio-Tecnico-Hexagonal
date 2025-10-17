package com.inventory.domain.model;

import com.inventory.domain.event.DomainEvent;
import java.util.List;

public record ReservationResult(
    ReservationId reservationId,
    Reservation reservation,
    List<DomainEvent> events
) {
}

