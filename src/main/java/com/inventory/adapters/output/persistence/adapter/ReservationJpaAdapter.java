package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import com.inventory.adapters.output.persistence.mapper.ReservationPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.ReservationJpaRepository;
import com.inventory.application.port.output.ReservationRepository;
import com.inventory.domain.model.Reservation;
import com.inventory.domain.model.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationJpaAdapter implements ReservationRepository {
    
    private final ReservationJpaRepository jpaRepository;
    private final ReservationPersistenceMapper mapper;
    
    @Override
    public Optional<Reservation> findById(String reservationId) {
        log.debug("Finding reservation: {}", reservationId);
        
        return jpaRepository
            .findById(reservationId)
            .map(mapper::toDomain);
    }
    
    @Override
    public Reservation save(Reservation reservation) {
        log.debug("Saving reservation: {}, status: {}", 
            reservation.getId(), reservation.getStatus());
        
        var entity = mapper.toEntity(reservation);
        var saved = jpaRepository.save(entity);
        
        return mapper.toDomain(saved);
    }
    
    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        var entityStatus = ReservationStatusEntity.valueOf(status.name());
        
        return jpaRepository
            .findByStatus(entityStatus)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<Reservation> findExpiredReservations(LocalDateTime before) {
        return jpaRepository
            .findExpiredReservations(before)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public void delete(Reservation reservation) {
        log.debug("Deleting reservation: {}", reservation.getId());
        jpaRepository.deleteById(reservation.getId());
    }
}

