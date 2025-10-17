package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockReleasedEvent;
import com.inventory.domain.exception.*;
import com.inventory.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReleaseStockService implements ReleaseStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    
    @Override
    public Result<Void, DomainError> release(ReleaseStockCommand command) {
        
        log.info("Releasing reservation: {}, reason: {}", 
            command.reservationId(), command.reason());
        
        try {
            // 1. Load reservation
            Reservation reservation = reservationRepository
                .findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(
                    command.reservationId()
                ));
            
            // 2. Validate can be released
            if (reservation.getStatus() == ReservationStatus.COMMITTED) {
                throw new InvalidReservationStateException(
                    command.reservationId(),
                    reservation.getStatus(),
                    ReservationStatus.RESERVED
                );
            }
            
            // 3. Load inventory with lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(
                    reservation.getStoreId(),
                    reservation.getSku()
                )
                .orElseThrow(() -> new ProductNotFoundException(
                    reservation.getSku(),
                    reservation.getStoreId()
                ));
            
            // 4. Execute domain operation (return to available)
            inventory.release(reservation.getQuantity());
            
            // 5. Update reservation
            Reservation releasedReservation = reservation.withStatus(
                ReservationStatus.CANCELLED
            );
            
            // 6. Persist
            inventoryRepository.save(inventory);
            reservationRepository.save(releasedReservation);
            
            // 7. Create and publish event
            StockReleasedEvent event = StockReleasedEvent.create(
                command.reservationId(),
                reservation.getStoreId(),
                reservation.getSku(),
                reservation.getQuantity(),
                command.reason()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Reservation released successfully");
            
            return Result.success(null);
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during release: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during release", ex);
            return Result.failure(DomainError.of(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
            ));
        }
    }
}

