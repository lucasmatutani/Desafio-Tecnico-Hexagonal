package com.inventory.adapters.output.messaging;

import com.inventory.application.port.output.EventPublisher;
import com.inventory.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryEventPublisher implements EventPublisher {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            
            log.info("📤 EVENT PUBLISHED: {} ({})", 
                event.eventType(), event.eventId());
            log.debug("Event payload: {}", eventJson);
            
            // TODO: Integrar com SNS/SQS quando disponível
            // snsClient.publish(...)
            
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.eventId(), e);
            // Em produção, considerar DLQ ou retry
        }
    }
    
    @Override
    public void publishBatch(List<DomainEvent> events) {
        log.info("📤 Publishing batch of {} events", events.size());
        events.forEach(this::publish);
    }
}

