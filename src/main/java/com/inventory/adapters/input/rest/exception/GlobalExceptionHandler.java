package com.inventory.adapters.input.rest.exception;

import com.inventory.adapters.input.rest.dto.ErrorResponse;
import com.inventory.adapters.input.rest.dto.ErrorResponse.FieldErrorDetail;
import com.inventory.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex, 
            HttpServletRequest request) {
        
        log.error("Domain exception: {} - {}", ex.getCode(), ex.getMessage(), ex);
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getCode(),
            ex.getMessage(),
            request.getRequestURI(),
            ex.getDetails(),
            List.of()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.error("Validation failed: {}", ex.getMessage());
        
        List<FieldErrorDetail> fieldErrors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                Object rejectedValue = ((FieldError) error).getRejectedValue();
                return new FieldErrorDetail(fieldName, message, rejectedValue);
            })
            .toList();
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Request validation failed",
            request.getRequestURI(),
            Map.of("errorCount", fieldErrors.size()),
            fieldErrors
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        log.error("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_ARGUMENT",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Unexpected error", ex);
        
        ErrorResponse response = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_ERROR",
            "An unexpected error occurred. Please try again later.",
            request.getRequestURI(),
            Map.of("type", ex.getClass().getSimpleName())
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}

