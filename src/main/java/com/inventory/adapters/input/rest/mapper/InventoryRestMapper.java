package com.inventory.adapters.input.rest.mapper;

import com.inventory.adapters.input.rest.dto.*;
import com.inventory.application.port.input.*;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryRestMapper {
    
    public ReserveStockCommand toCommand(ReserveStockRequest request) {
        return new ReserveStockCommand(
            StoreId.of(request.storeId()),
            Sku.of(request.sku()),
            request.quantity(),
            request.customerId()
        );
    }
    
    public CommitStockCommand toCommand(CommitStockRequest request) {
        return new CommitStockCommand(
            request.reservationId(),
            request.orderId()
        );
    }
    
    public ReleaseStockCommand toCommand(ReleaseStockRequest request) {
        return new ReleaseStockCommand(
            request.reservationId(),
            request.reason()
        );
    }
    
    public ReservationResponse toReservationResponse(
            String reservationId,
            ReserveStockCommand command,
            LocalDateTime expiresAt) {
        return new ReservationResponse(
            reservationId,
            command.storeId().value(),
            command.sku().value(),
            command.quantity(),
            "RESERVED",
            expiresAt,
            "Stock reserved successfully. Complete your purchase before expiration."
        );
    }
    
    public ReservationResponse toCommitResponse(String orderId) {
        return new ReservationResponse(
            null,
            null,
            null,
            0,
            "COMMITTED",
            null,
            "Stock committed successfully. OrderId: " + orderId
        );
    }
    
    public InventoryResponse toInventoryResponse(InventoryView view) {
        return new InventoryResponse(
            view.storeId(),
            view.sku(),
            view.productName(),
            view.availableStock(),
            view.reservedStock(),
            view.soldStock(),
            view.availableStock() + view.reservedStock()
        );
    }
}

