package com.inventory.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class SkuTest {
    
    @Test
    void shouldCreateValidSku() {
        // Given
        String validSkuValue = "SKU123";
        
        // When
        Sku sku = Sku.of(validSkuValue);
        
        // Then
        assertThat(sku).isNotNull();
        assertThat(sku.value()).isEqualTo(validSkuValue);
        assertThat(sku.toString()).isEqualTo(validSkuValue);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SKU123", "SKU1234", "SKU12345", "SKU123456"})
    void shouldAcceptValidSkuFormats(String validSku) {
        // When/Then
        assertThatCode(() -> Sku.of(validSku))
            .doesNotThrowAnyException();
    }
    
    @Test
    void shouldRejectNullSku() {
        // When/Then
        assertThatThrownBy(() -> Sku.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("SKU cannot be null");
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "SKU", "SKU12", "SKU1234567", "ABC123", "sku123"})
    void shouldRejectInvalidSkuFormats(String invalidSku) {
        // When/Then
        assertThatThrownBy(() -> Sku.of(invalidSku))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("SKU must be in format");
    }
    
    @Test
    void shouldBeEqualWhenSameValue() {
        // Given
        Sku sku1 = Sku.of("SKU123");
        Sku sku2 = Sku.of("SKU123");
        
        // Then
        assertThat(sku1).isEqualTo(sku2);
        assertThat(sku1.hashCode()).isEqualTo(sku2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        // Given
        Sku sku1 = Sku.of("SKU123");
        Sku sku2 = Sku.of("SKU456");
        
        // Then
        assertThat(sku1).isNotEqualTo(sku2);
    }
}

