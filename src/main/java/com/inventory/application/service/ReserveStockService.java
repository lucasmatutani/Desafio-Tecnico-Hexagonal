package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockReservedEvent;
import com.inventory.domain.exception.DomainException;
import com.inventory.domain.exception.ProductNotFoundException;
import com.inventory.domain.model.*;
import com.inventory.domain.policy.ReservationPolicy;
import com.inventory.domain.policy.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReserveStockService implements ReserveStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    private final ReservationPolicy reservationPolicy;
    
    @Override
    public Result<ReservationId, DomainError> reserve(ReserveStockCommand command) {
        
        log.info("Reserving stock - store: {}, sku: {}, quantity: {}, customer: {}", 
            command.storeId(), command.sku(), command.quantity(), command.customerId());
        
        try {
            // 1. Load aggregate with pessimistic lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(command.storeId(), command.sku())
                .orElseThrow(() -> new ProductNotFoundException(
                    command.sku(), 
                    command.storeId()
                ));
            
            log.debug("Inventory loaded - available: {}, reserved: {}", 
                inventory.availableStock(), inventory.reservedStock());
            
            // 2. Validate business rules
            ValidationResult validation = reservationPolicy.validate(
                inventory, 
                command.quantity()
            );
            
            if (validation.hasErrors()) {
                log.warn("Validation failed: {}", validation.errors());
                return Result.failure(new DomainError(
                    "VALIDATION_ERROR",
                    "Reservation validation failed",
                    Map.of("errors", validation.errors())
                ));
            }
            
            // 3. Execute domain operation
            ReservationId reservationId = ReservationId.generate();
            inventory.reserve(command.quantity());
            
            // 4. Create reservation
            Reservation reservation = Reservation.builder()
                .id(reservationId.value())
                .storeId(command.storeId())
                .sku(command.sku())
                .quantity(command.quantity())
                .customerId(command.customerId())
                .status(ReservationStatus.RESERVED)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(reservationPolicy.getTtl()))
                .build();
            
            // 5. Persist changes
            inventoryRepository.save(inventory);
            reservationRepository.save(reservation);
            
            // 6. Create and publish domain event
            StockReservedEvent event = StockReservedEvent.create(
                reservationId.value(),
                command.storeId(),
                command.sku(),
                command.quantity(),
                command.customerId()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Stock reserved successfully - reservationId: {}, expiresAt: {}", 
                reservationId, reservation.getExpiresAt());
            
            return Result.success(reservationId);
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during reservation: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during reservation", ex);
            return Result.failure(new DomainError(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                Map.of("error", ex.getMessage())
            ));
        }
    }
}

