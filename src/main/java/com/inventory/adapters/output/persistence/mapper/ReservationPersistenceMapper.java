package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.ReservationEntity;
import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import com.inventory.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReservationPersistenceMapper {
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "storeIdToString")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "skuToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToEntity")
    ReservationEntity toEntity(Reservation domain);
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "stringToStoreId")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "stringToSku")
    @Mapping(target = "status", source = "status", qualifiedByName = "entityToStatus")
    Reservation toDomain(ReservationEntity entity);
    
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
    
    @Named("statusToEntity")
    default ReservationStatusEntity statusToEntity(ReservationStatus status) {
        return status != null ? ReservationStatusEntity.valueOf(status.name()) : null;
    }
    
    @Named("entityToStatus")
    default ReservationStatus entityToStatus(ReservationStatusEntity status) {
        return status != null ? ReservationStatus.valueOf(status.name()) : null;
    }
}

