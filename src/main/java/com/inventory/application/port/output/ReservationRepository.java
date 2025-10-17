package com.inventory.application.port.output;

import com.inventory.domain.model.Reservation;
import com.inventory.domain.model.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> findById(String reservationId);
    Reservation save(Reservation reservation);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findExpiredReservations(LocalDateTime before);
    void delete(Reservation reservation);
}

