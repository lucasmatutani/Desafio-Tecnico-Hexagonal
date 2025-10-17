package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.ReservationEntity;
import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {
    
    List<ReservationEntity> findByStatus(ReservationStatusEntity status);
    
    @Query("SELECT r FROM ReservationEntity r WHERE r.status = 'RESERVED' AND r.expiresAt < :before")
    List<ReservationEntity> findExpiredReservations(@Param("before") LocalDateTime before);
}

