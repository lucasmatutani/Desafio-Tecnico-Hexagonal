package com.inventory.application.port.output;

import com.inventory.domain.event.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventStore {
    void store(DomainEvent event);
    List<DomainEvent> findByAggregateId(String aggregateId);
    List<DomainEvent> findByAggregateIdAndTimestamp(
        String aggregateId, 
        LocalDateTime from, 
        LocalDateTime to
    );
    Optional<DomainEvent> findByEventId(String eventId);
    List<DomainEvent> findAll();
}

