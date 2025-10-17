package com.inventory.application.port.output;

import com.inventory.domain.event.DomainEvent;
import java.util.List;

public interface EventPublisher {
    void publish(DomainEvent event);
    void publishBatch(List<DomainEvent> events);
}

