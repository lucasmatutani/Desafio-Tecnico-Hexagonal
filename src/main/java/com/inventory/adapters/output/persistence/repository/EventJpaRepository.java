package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, String> {
    
    List<EventEntity> findByAggregateIdOrderByTimestampAsc(String aggregateId);
    
    @Query("SELECT e FROM EventEntity e WHERE e.aggregateId = :aggregateId " +
           "AND e.timestamp BETWEEN :from AND :to ORDER BY e.timestamp ASC")
    List<EventEntity> findByAggregateIdAndTimestampBetween(
        @Param("aggregateId") String aggregateId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );
    
    List<EventEntity> findByEventTypeOrderByTimestampDesc(String eventType);
}

