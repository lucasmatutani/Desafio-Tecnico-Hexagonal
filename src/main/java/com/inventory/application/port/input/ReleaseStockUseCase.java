package com.inventory.application.port.input;

public interface ReleaseStockUseCase {
    Result<Void, DomainError> release(ReleaseStockCommand command);
}

