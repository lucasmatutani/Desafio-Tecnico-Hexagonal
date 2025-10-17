package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import com.inventory.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface InventoryPersistenceMapper {
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "storeIdToString")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "skuToString")
    @Mapping(target = "availableStock", source = "stock", qualifiedByName = "stockToAvailable")
    @Mapping(target = "reservedStock", source = "stock", qualifiedByName = "stockToReserved")
    @Mapping(target = "soldStock", source = "stock", qualifiedByName = "stockToSold")
    @Mapping(target = "version", ignore = true)
    InventoryEntity toEntity(Inventory domain);
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "stringToStoreId")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "stringToSku")
    @Mapping(target = "stock", expression = "java(mapToStock(entity))")
    Inventory toDomain(InventoryEntity entity);
    
    // Value Object conversions
    @Named("storeIdToString")
    default String storeIdToString(StoreId storeId) {
        return storeId != null ? storeId.value() : null;
    }
    
    @Named("stringToStoreId")
    default StoreId stringToStoreId(String storeId) {
        return storeId != null ? StoreId.of(storeId) : null;
    }
    
    @Named("skuToString")
    default String skuToString(Sku sku) {
        return sku != null ? sku.value() : null;
    }
    
    @Named("stringToSku")
    default Sku stringToSku(String sku) {
        return sku != null ? Sku.of(sku) : null;
    }
    
    @Named("stockToAvailable")
    default Integer stockToAvailable(Stock stock) {
        return stock != null ? stock.availableStock() : 0;
    }
    
    @Named("stockToReserved")
    default Integer stockToReserved(Stock stock) {
        return stock != null ? stock.reservedStock() : 0;
    }
    
    @Named("stockToSold")
    default Integer stockToSold(Stock stock) {
        return stock != null ? stock.soldStock() : 0;
    }
    
    default Stock mapToStock(InventoryEntity entity) {
        return new Stock(
            entity.getAvailableStock(),
            entity.getReservedStock(),
            entity.getSoldStock()
        );
    }
}

