package com.inventory.adapters.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, Object> details,
    List<FieldErrorDetail> fieldErrors
) {
    
    public record FieldErrorDetail(
        String field,
        String message,
        Object rejectedValue
    ) {
    }
    
    public static ErrorResponse of(
            int status,
            String error,
            String message,
            String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            Map.of(),
            List.of()
        );
    }
    
    public static ErrorResponse of(
            int status,
            String error,
            String message,
            String path,
            Map<String, Object> details) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            details,
            List.of()
        );
    }
}

