package com.inventory.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    String eventId();
    String eventType();
    LocalDateTime timestamp();
    String aggregateId();
}
