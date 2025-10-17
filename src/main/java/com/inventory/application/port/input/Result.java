package com.inventory.application.port.input;

public sealed interface Result<S, F> 
    permits Result.Success, Result.Failure {
    
    record Success<S, F>(S value) implements Result<S, F> {
        public boolean isSuccess() { return true; }
        public boolean isFailure() { return false; }
        
        public S getValue() { return value; }
        public F getError() { throw new UnsupportedOperationException("Success has no error"); }
    }
    
    record Failure<S, F>(F error) implements Result<S, F> {
        public boolean isSuccess() { return false; }
        public boolean isFailure() { return true; }
        
        public S getValue() { throw new UnsupportedOperationException("Failure has no value"); }
        public F getError() { return error; }
    }
    
    static <S, F> Result<S, F> success(S value) {
        return new Success<>(value);
    }
    
    static <S, F> Result<S, F> failure(F error) {
        return new Failure<>(error);
    }
    
    boolean isSuccess();
    boolean isFailure();
    S getValue();
    F getError();
}

