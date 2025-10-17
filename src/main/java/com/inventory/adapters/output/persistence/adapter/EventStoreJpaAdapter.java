package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.mapper.EventPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.EventJpaRepository;
import com.inventory.application.port.output.EventStore;
import com.inventory.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStoreJpaAdapter implements EventStore {
    
    private final EventJpaRepository jpaRepository;
    private final EventPersistenceMapper mapper;
    
    @Override
    public void store(DomainEvent event) {
        log.debug("Storing event: {} ({})", event.eventId(), event.eventType());
        
        var entity = mapper.toEntity(event);
        jpaRepository.save(entity);
        
        log.info("Event stored: {}", event.eventId());
    }
    
    @Override
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        log.debug("Finding events by aggregateId: {}", aggregateId);
        
        return jpaRepository
            .findByAggregateIdOrderByTimestampAsc(aggregateId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<DomainEvent> findByAggregateIdAndTimestamp(
            String aggregateId, 
            LocalDateTime from, 
            LocalDateTime to) {
        
        log.debug("Finding events by aggregateId: {} between {} and {}", 
            aggregateId, from, to);
        
        return jpaRepository
            .findByAggregateIdAndTimestampBetween(aggregateId, from, to)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public Optional<DomainEvent> findByEventId(String eventId) {
        return jpaRepository
            .findById(eventId)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<DomainEvent> findAll() {
        return jpaRepository
            .findAll()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}

