package com.inventory.application.port.input;

public interface CommitStockUseCase {
    Result<String, DomainError> commit(CommitStockCommand command);
}

