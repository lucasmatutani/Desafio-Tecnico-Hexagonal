package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.EventEntity;
import com.inventory.domain.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPersistenceMapper {
    
    private final ObjectMapper objectMapper;
    
    public EventEntity toEntity(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            
            return EventEntity.builder()
                .eventId(event.eventId())
                .eventType(event.eventType())
                .aggregateId(event.aggregateId())
                .aggregateType("Inventory")
                .payload(payload)
                .timestamp(event.timestamp())
                .version(1)
                .build();
                
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
    
    public DomainEvent toDomain(EventEntity entity) {
        try {
            // Simple deserialization based on event type
            return switch (entity.getEventType()) {
                case "StockReserved" -> 
                    objectMapper.readValue(entity.getPayload(), StockReservedEvent.class);
                case "StockCommitted" -> 
                    objectMapper.readValue(entity.getPayload(), StockCommittedEvent.class);
                case "StockReleased" -> 
                    objectMapper.readValue(entity.getPayload(), StockReleasedEvent.class);
                case "StockAdded" -> 
                    objectMapper.readValue(entity.getPayload(), StockAddedEvent.class);
                default -> 
                    throw new IllegalArgumentException("Unknown event type: " + entity.getEventType());
            };
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}

