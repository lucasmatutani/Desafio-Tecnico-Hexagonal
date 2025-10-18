package com.inventory.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReservationIdTest {
    
    @Test
    void shouldGenerateUniqueId() {
        // When
        ReservationId id1 = ReservationId.generate();
        ReservationId id2 = ReservationId.generate();
        
        // Then
        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.value()).startsWith("RES-");
    }
    
    @Test
    void shouldCreateFromValue() {
        // Given
        String value = "RES-123";
        
        // When
        ReservationId id = ReservationId.of(value);
        
        // Then
        assertThat(id.value()).isEqualTo(value);
    }
    
    @Test
    void shouldRejectNullValue() {
        // When/Then
        assertThatThrownBy(() -> ReservationId.of(null))
            .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    void shouldRejectBlankValue() {
        // When/Then
        assertThatThrownBy(() -> ReservationId.of(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

