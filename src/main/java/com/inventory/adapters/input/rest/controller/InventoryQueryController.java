package com.inventory.adapters.input.rest.controller;

import com.inventory.adapters.input.rest.dto.ErrorResponse;
import com.inventory.adapters.input.rest.dto.InventoryResponse;
import com.inventory.adapters.input.rest.mapper.InventoryRestMapper;
import com.inventory.application.port.input.QueryStockUseCase;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Queries", description = "Read operations for inventory management")
public class InventoryQueryController {
    
    private final QueryStockUseCase queryStockUseCase;
    private final InventoryRestMapper mapper;
    
    @GetMapping("/{storeId}/{sku}")
    @Operation(
        summary = "Get stock information", 
        description = "Query current stock levels for a specific product in a store. Returns available, reserved and sold quantities."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InventoryResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid storeId or SKU format",
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
    public ResponseEntity<?> getStock(
            @Parameter(description = "Store identifier (e.g., STORE-01)", example = "STORE-01")
            @PathVariable String storeId,
            @Parameter(description = "Product SKU in format SKUxxx (e.g., SKU123)", example = "SKU123")
            @PathVariable String sku) {
        
        log.info("📥 Query stock - store: {}, sku: {}", storeId, sku);
        
        try {
            var result = queryStockUseCase.findByStoreAndSku(
                StoreId.of(storeId),
                Sku.of(sku)
            );
            
            if (result.isPresent()) {
                var response = mapper.toInventoryResponse(result.get());
                log.debug("✅ Stock found - available: {}", response.availableStock());
                return ResponseEntity.ok(response);
            } else {
                log.warn("❌ Stock not found");
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "PRODUCT_NOT_FOUND",
                        String.format("Product %s not found in store %s", sku, storeId),
                        String.format("/api/v1/inventory/%s/%s", storeId, sku)
                    ));
            }
            
        } catch (IllegalArgumentException ex) {
            log.error("❌ Invalid input: {}", ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    "INVALID_INPUT",
                    ex.getMessage(),
                    String.format("/api/v1/inventory/%s/%s", storeId, sku)
                ));
        }
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Health check", 
        description = "Simple health check endpoint to verify if the service is running and responding"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Service is healthy and running",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Inventory Service is running");
    }
}

