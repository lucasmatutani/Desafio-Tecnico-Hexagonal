package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockCommittedEvent;
import com.inventory.domain.exception.*;
import com.inventory.domain.model.*;
import com.inventory.domain.policy.ExpirationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommitStockService implements CommitStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    private final ExpirationPolicy expirationPolicy;
    
    @Override
    public Result<String, DomainError> commit(CommitStockCommand command) {
        
        log.info("Committing reservation: {}", command.reservationId());
        
        try {
            // 1. Load reservation
            Reservation reservation = reservationRepository
                .findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(
                    command.reservationId()
                ));
            
            log.debug("Reservation found - status: {}, quantity: {}", 
                reservation.getStatus(), reservation.getQuantity());
            
            // 2. Validate state
            if (reservation.getStatus() != ReservationStatus.RESERVED) {
                throw new InvalidReservationStateException(
                    command.reservationId(),
                    reservation.getStatus(),
                    ReservationStatus.RESERVED
                );
            }
            
            // 3. Check expiration
            if (expirationPolicy.isExpired(reservation)) {
                throw new ReservationExpiredException(
                    command.reservationId(),
                    reservation.getExpiresAt(),
                    15
                );
            }
            
            // 4. Load inventory with lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(
                    reservation.getStoreId(),
                    reservation.getSku()
                )
                .orElseThrow(() -> new ProductNotFoundException(
                    reservation.getSku(),
                    reservation.getStoreId()
                ));
            
            // 5. Execute domain operation (reserved → sold)
            inventory.commit(reservation.getQuantity());
            
            // 6. Update reservation status
            Reservation committedReservation = reservation.withStatus(
                ReservationStatus.COMMITTED
            );
            
            // 7. Persist
            inventoryRepository.save(inventory);
            reservationRepository.save(committedReservation);
            
            // 8. Create and publish event
            StockCommittedEvent event = StockCommittedEvent.create(
                command.reservationId(),
                reservation.getStoreId(),
                reservation.getSku(),
                reservation.getQuantity(),
                reservation.getCustomerId()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Reservation committed successfully - orderId: {}", 
                command.orderId());
            
            return Result.success(command.orderId());
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during commit: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during commit", ex);
            return Result.failure(DomainError.of(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
            ));
        }
    }
}

