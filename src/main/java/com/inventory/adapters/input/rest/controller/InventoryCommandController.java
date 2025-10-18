package com.inventory.adapters.input.rest.controller;

import com.inventory.adapters.input.rest.dto.*;
import com.inventory.adapters.input.rest.mapper.InventoryRestMapper;
import com.inventory.application.port.input.*;
import com.inventory.domain.model.ReservationId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Commands", description = "Write operations for inventory management")
public class InventoryCommandController {
    
    private final ReserveStockUseCase reserveStockUseCase;
    private final CommitStockUseCase commitStockUseCase;
    private final ReleaseStockUseCase releaseStockUseCase;
    private final InventoryRestMapper mapper;
    
    @PostMapping("/reserve")
    @Operation(
        summary = "Reserve stock", 
        description = "Reserve stock for a customer with 15 minutes TTL. Returns reservation ID that expires automatically."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Stock reserved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or insufficient stock",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found in store",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<?> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
        
        log.info("📥 Reserve stock request - store: {}, sku: {}, qty: {}", 
            request.storeId(), request.sku(), request.quantity());
        
        var command = mapper.toCommand(request);
        var result = reserveStockUseCase.reserve(command);
        
        if (result.isSuccess()) {
            ReservationId reservationId = result.getValue();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);
            
            var response = mapper.toReservationResponse(
                reservationId.value(),
                command,
                expiresAt
            );
            
            log.info("✅ Stock reserved - reservationId: {}", reservationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            log.warn("❌ Reservation failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/reserve",
                    result.getError().details()
                ));
        }
    }
    
    @PostMapping("/commit")
    @Operation(
        summary = "Commit reservation", 
        description = "Confirm sale and commit reserved stock. Moves stock from reserved to sold."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation committed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid reservation state or expired reservation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<?> commitStock(@Valid @RequestBody CommitStockRequest request) {
        
        log.info("📥 Commit stock request - reservationId: {}", request.reservationId());
        
        var command = mapper.toCommand(request);
        var result = commitStockUseCase.commit(command);
        
        if (result.isSuccess()) {
            String orderId = result.getValue();
            var response = mapper.toCommitResponse(orderId);
            
            log.info("✅ Stock committed - orderId: {}", orderId);
            return ResponseEntity.ok(response);
        } else {
            log.warn("❌ Commit failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/commit",
                    result.getError().details()
                ));
        }
    }
    
    @PostMapping("/release")
    @Operation(
        summary = "Release reservation", 
        description = "Cancel reservation and return stock to available. Can be used when customer cancels order."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation released successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid reservation state (already committed)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<?> releaseStock(@Valid @RequestBody ReleaseStockRequest request) {
        
        log.info("📥 Release stock request - reservationId: {}", request.reservationId());
        
        var command = mapper.toCommand(request);
        var result = releaseStockUseCase.release(command);
        
        if (result.isSuccess()) {
            log.info("✅ Stock released");
            return ResponseEntity.ok(new ReservationResponse(
                request.reservationId(),
                null, null, 0,
                "CANCELLED",
                null,
                "Reservation cancelled successfully"
            ));
        } else {
            log.warn("❌ Release failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/release",
                    result.getError().details()
                ));
        }
    }
}

