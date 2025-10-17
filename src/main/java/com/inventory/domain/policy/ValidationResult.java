package com.inventory.domain.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ValidationResult(
    boolean valid,
    List<String> errors
) {
    
    public static ValidationResult success() {
        return new ValidationResult(true, List.of());
    }
    
    public static ValidationResult failure(String... errors) {
        return new ValidationResult(false, Arrays.asList(errors));
    }
    
    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, new ArrayList<>(errors));
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public boolean hasErrors() {
        return !valid;
    }
}

