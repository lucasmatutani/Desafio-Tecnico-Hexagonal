package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockReservedEvent;
import com.inventory.domain.model.*;
import com.inventory.domain.policy.ReservationPolicy;
import com.inventory.domain.policy.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveStockServiceTest {
    
    @Mock
    private InventoryRepository inventoryRepository;
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private EventStore eventStore;
    
    @Mock
    private ReservationPolicy reservationPolicy;
    
    @InjectMocks
    private ReserveStockService service;
    
    private StoreId storeId;
    private Sku sku;
    
    @BeforeEach
    void setUp() {
        storeId = StoreId.of("STORE-01");
        sku = Sku.of("SKU123");
    }
    
    @Test
    void shouldReserveStockSuccessfully() {
        // Given
        ReserveStockCommand command = new ReserveStockCommand(
            storeId, sku, 10, "CUST-001"
        );
        
        Inventory inventory = createInventory(100, 0, 0);
        
        when(inventoryRepository.findByStoreIdAndSkuWithLock(storeId, sku))
            .thenReturn(Optional.of(inventory));
        when(reservationPolicy.validate(inventory, 10))
            .thenReturn(ValidationResult.success());
        when(reservationPolicy.getTtl())
            .thenReturn(Duration.ofMinutes(15));
        when(inventoryRepository.save(any(Inventory.class)))
            .thenReturn(inventory);
        when(reservationRepository.save(any(Reservation.class)))
            .thenAnswer(inv -> inv.getArgument(0));
        
        // When
        Result<ReservationId, DomainError> result = service.reserve(command);
        
        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isNotNull();
        assertThat(result.getValue().value()).startsWith("RES-");
        
        // Verify interactions
        verify(inventoryRepository).findByStoreIdAndSkuWithLock(storeId, sku);
        verify(inventoryRepository).save(any(Inventory.class));
        verify(reservationRepository).save(any(Reservation.class));
        verify(eventStore).store(any(StockReservedEvent.class));
        verify(eventPublisher).publish(any(StockReservedEvent.class));
        
        // Verify inventory was updated
        assertThat(inventory.availableStock()).isEqualTo(90);
        assertThat(inventory.reservedStock()).isEqualTo(10);
    }
    
    @Test
    void shouldFailWhenProductNotFound() {
        // Given
        ReserveStockCommand command = new ReserveStockCommand(
            storeId, sku, 10, "CUST-001"
        );
        
        when(inventoryRepository.findByStoreIdAndSkuWithLock(storeId, sku))
            .thenReturn(Optional.empty());
        
        // When
        Result<ReservationId, DomainError> result = service.reserve(command);
        
        // Then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo("PRODUCT_NOT_FOUND");
        
        // Verify no side effects
        verify(inventoryRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
    
    @Test
    void shouldFailWhenValidationFails() {
        // Given
        ReserveStockCommand command = new ReserveStockCommand(
            storeId, sku, 200, "CUST-001"
        );
        
        Inventory inventory = createInventory(100, 0, 0);
        
        when(inventoryRepository.findByStoreIdAndSkuWithLock(storeId, sku))
            .thenReturn(Optional.of(inventory));
        when(reservationPolicy.validate(inventory, 200))
            .thenReturn(ValidationResult.failure("Insufficient stock"));
        
        // When
        Result<ReservationId, DomainError> result = service.reserve(command);
        
        // Then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo("VALIDATION_ERROR");
        
        verify(inventoryRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
    
    @Test
    void shouldCaptureAndPublishCorrectEvent() {
        // Given
        ReserveStockCommand command = new ReserveStockCommand(
            storeId, sku, 10, "CUST-001"
        );
        
        Inventory inventory = createInventory(100, 0, 0);
        
        when(inventoryRepository.findByStoreIdAndSkuWithLock(storeId, sku))
            .thenReturn(Optional.of(inventory));
        when(reservationPolicy.validate(inventory, 10))
            .thenReturn(ValidationResult.success());
        when(reservationPolicy.getTtl())
            .thenReturn(Duration.ofMinutes(15));
        when(inventoryRepository.save(any())).thenReturn(inventory);
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        
        ArgumentCaptor<StockReservedEvent> eventCaptor = 
            ArgumentCaptor.forClass(StockReservedEvent.class);
        
        // When
        service.reserve(command);
        
        // Then
        verify(eventPublisher).publish(eventCaptor.capture());
        
        StockReservedEvent event = eventCaptor.getValue();
        assertThat(event.eventType()).isEqualTo("StockReserved");
        assertThat(event.storeId()).isEqualTo(storeId);
        assertThat(event.sku()).isEqualTo(sku);
        assertThat(event.quantity()).isEqualTo(10);
        assertThat(event.customerId()).isEqualTo("CUST-001");
    }
    
    private Inventory createInventory(int available, int reserved, int sold) {
        return Inventory.builder()
            .id(1L)
            .storeId(storeId)
            .sku(sku)
            .productName("Test Product")
            .stock(new Stock(available, reserved, sold))
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}

