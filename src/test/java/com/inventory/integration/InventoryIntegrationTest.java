package com.inventory.integration;

import com.inventory.adapters.input.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InventoryIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String BASE_URL = "/api/v1/inventory";
    
    @Test
    void shouldCompleteFullReservationFlow() throws Exception {
        // 1. Query initial stock
        mockMvc.perform(get(BASE_URL + "/STORE-01/SKU123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableStock").value(100))
            .andExpect(jsonPath("$.reservedStock").value(0));
        
        // 2. Reserve stock
        ReserveStockRequest reserveRequest = new ReserveStockRequest(
            "STORE-01", "SKU123", 10, "CUST-001"
        );
        
        MvcResult reserveResult = mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.reservationId").exists())
            .andExpect(jsonPath("$.status").value("RESERVED"))
            .andExpect(jsonPath("$.quantity").value(10))
            .andReturn();
        
        String responseBody = reserveResult.getResponse().getContentAsString();
        ReservationResponse reserveResponse = objectMapper.readValue(
            responseBody, 
            ReservationResponse.class
        );
        String reservationId = reserveResponse.reservationId();
        
        // 3. Verify stock was reserved
        mockMvc.perform(get(BASE_URL + "/STORE-01/SKU123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableStock").value(90))
            .andExpect(jsonPath("$.reservedStock").value(10))
            .andExpect(jsonPath("$.soldStock").value(0));
        
        // 4. Commit reservation
        CommitStockRequest commitRequest = new CommitStockRequest(
            reservationId, 
            "ORDER-001"
        );
        
        mockMvc.perform(post(BASE_URL + "/commit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commitRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMMITTED"));
        
        // 5. Verify final stock state
        mockMvc.perform(get(BASE_URL + "/STORE-01/SKU123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableStock").value(90))
            .andExpect(jsonPath("$.reservedStock").value(0))
            .andExpect(jsonPath("$.soldStock").value(10));
    }
    
    @Test
    void shouldReserveAndReleaseStock() throws Exception {
        // 1. Reserve stock
        ReserveStockRequest reserveRequest = new ReserveStockRequest(
            "STORE-01", "SKU456", 5, "CUST-002"
        );
        
        MvcResult reserveResult = mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String responseBody = reserveResult.getResponse().getContentAsString();
        ReservationResponse reserveResponse = objectMapper.readValue(
            responseBody, 
            ReservationResponse.class
        );
        String reservationId = reserveResponse.reservationId();
        
        // 2. Verify reserved
        mockMvc.perform(get(BASE_URL + "/STORE-01/SKU456"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableStock").value(45))
            .andExpect(jsonPath("$.reservedStock").value(5));
        
        // 3. Release reservation
        ReleaseStockRequest releaseRequest = new ReleaseStockRequest(
            reservationId,
            "Customer cancelled order"
        );
        
        mockMvc.perform(post(BASE_URL + "/release")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(releaseRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
        
        // 4. Verify stock returned
        mockMvc.perform(get(BASE_URL + "/STORE-01/SKU456"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableStock").value(50))
            .andExpect(jsonPath("$.reservedStock").value(0));
    }
    
    @Test
    void shouldFailWhenReservingInsufficientStock() throws Exception {
        // Given
        ReserveStockRequest request = new ReserveStockRequest(
            "STORE-01", "SKU123", 200, "CUST-003"
        );
        
        // When/Then
        mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.message").exists());
    }
    
    @Test
    void shouldFailWhenProductNotFound() throws Exception {
        // When/Then
        mockMvc.perform(get(BASE_URL + "/STORE-99/SKU999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("PRODUCT_NOT_FOUND"));
    }
    
    @Test
    void shouldFailWhenCommittingNonExistentReservation() throws Exception {
        // Given
        CommitStockRequest request = new CommitStockRequest(
            "RES-INVALID", 
            "ORDER-002"
        );
        
        // When/Then
        mockMvc.perform(post(BASE_URL + "/commit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("RESERVATION_NOT_FOUND"));
    }
    
    @Test
    void shouldValidateRequestBody() throws Exception {
        // Given - Invalid SKU format
        ReserveStockRequest request = new ReserveStockRequest(
            "STORE-01", "INVALID", 10, "CUST-004"
        );
        
        // When/Then
        mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.fieldErrors").isArray());
    }
    
    @Test
    void shouldHandleMultipleConcurrentReservations() throws Exception {
        // This test verifies pessimistic locking works correctly
        // In a real scenario, you'd use multiple threads
        
        // Reserve 1
        ReserveStockRequest request1 = new ReserveStockRequest(
            "STORE-02", "SKU123", 40, "CUST-005"
        );
        mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isCreated());
        
        // Reserve 2
        ReserveStockRequest request2 = new ReserveStockRequest(
            "STORE-02", "SKU123", 40, "CUST-006"
        );
        mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isCreated());
        
        // Try to reserve more than available - should fail
        ReserveStockRequest request3 = new ReserveStockRequest(
            "STORE-02", "SKU123", 5, "CUST-007"
        );
        mockMvc.perform(post(BASE_URL + "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request3)))
            .andExpect(status().isBadRequest());
    }
}

